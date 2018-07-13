/*
 * (c) Copyright 2017 Micro Focus, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.cloudslang.tools.generator.services;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import io.cloudslang.tools.generator.entities.*;
import io.cloudslang.tools.generator.services.converters.ResultExpressionConverterService;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.jetbrains.annotations.NotNull;

import javax.management.BadAttributeValueExpException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.cloudslang.tools.generator.utils.NameUtils.getResultName;
import static io.cloudslang.tools.generator.utils.NameUtils.toSnakeCase;
import static java.lang.String.format;
import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;
import static org.apache.commons.lang3.StringUtils.repeat;
import static org.apache.commons.lang3.StringUtils.replace;
import static org.apache.commons.lang3.text.WordUtils.wrap;

@Slf4j
public class OperationService {

    private static final List<String> SESSION_OBJECTS = Arrays.asList("GlobalSessionObject", "SerializableSessionObject");
    private static final String CONTENT = ".content.";
    private static final String ACTIONS = ".actions.";
    private static final String DOT = ".";
    public static final String COMMENT_CHAR = "#!";
    public static final int MAX_LINE_LENGTH = 120;
    public static final String NEW_LINE = lineSeparator();

    public static CsOperationFile getOperation(String gav, @NotNull CtClass javaClass) throws ClassNotFoundException, NotFoundException, BadAttributeValueExpException {
        Optional<CtMethod> actionMethodOpt = getActionMethod(javaClass);
        if (actionMethodOpt.isPresent()) {
            Action action = (Action) actionMethodOpt.get().getAnnotation(Action.class);
            final List<CsInput> inputs = getInputs(actionMethodOpt.get());
            final String description = defaultIfEmpty(action.description(), "Generated description.");
            CsOperation operation = new CsOperation(
                    description,
                    replace(toSnakeCase(action.name()), " ", "_"),
                    inputs,
                    getOutputs(action, inputs),
                    getAction(gav, javaClass, actionMethodOpt.get()),
                    getResponses(action));

            final String namespace = javaClass.getPackageName()
                    .replace(CONTENT, DOT)
                    .replace(ACTIONS, DOT);
            return new CsOperationFile(namespace, operation, getOperationDescription(operation));
        }
        return null;
    }

    private static CsJavaAction getAction(String gav, CtClass javaClass, CtMethod actionMethod) {
        return new CsJavaAction(gav, javaClass.getName(), actionMethod.getName()); //todo this is wrong, should be checked
    }

    private static List<CsInput> getInputs(CtMethod actionMethod) throws ClassNotFoundException, NotFoundException, BadAttributeValueExpException {
        List<CsInput> inputs = new ArrayList<>();
        CtClass[] parameterTypes = actionMethod.getParameterTypes();
        Object[][] parameterAnnotations = actionMethod.getParameterAnnotations();
        if (parameterAnnotations.length != parameterTypes.length) {
            throw new BadAttributeValueExpException("All @Action method parameters need to be annotated with @Param");
        }
        for (int i = 0; i < parameterTypes.length; i++) {
            Object[] inputAnnotations = parameterAnnotations[i];
            CtClass parameterType = parameterTypes[i];
            if (!SESSION_OBJECTS.contains(parameterType.getSimpleName())) {
                for (Object annotation : inputAnnotations) {
                    if (annotation instanceof Param) {
                        Param param = (Param) annotation;
                        String name = toSnakeCase(param.value());
                        String description = param.description();
                        inputs.add(new CsInput(name, description, param.required(), null, param.encrypted(), false));
                        if (!name.equals(param.value())) {
                            inputs.add(new CsInput(param.value(), description, false, format("${get(\'%s\', \'\')}", name), param.encrypted(), true));
                        }
                    }
                }
            }
        }
        return inputs;
    }

    private static List<CsOutput> getOutputs(Action action, List<CsInput> inputs) {
        List<CsOutput> outputsList = new ArrayList<>();
        Output[] outputs = action.outputs();
        for (Output o : outputs) {
            String outputName = toSnakeCase(o.value());
            final String outputNameCopy = outputName;
            final String description = o.description();
            boolean hasInputWithSameName = inputs.stream().anyMatch(i -> i.getName().equals(outputNameCopy));
            if (hasInputWithSameName) {
                outputName += "_output";
            }
            String expression = "${get(\'" + o.value() + "\', \'\')}";
            outputsList.add(new CsOutput(outputName, description, expression));
        }
        return outputsList;
    }

    private static List<CsResponse> getResponses(Action action) {
        return Arrays.stream(action.responses())
                .map(r -> {
                    final String responseName = getResultName(r.text());
                    final String description = r.description();
                    if (!r.isDefault()) {
                        final String matchExpr = ResultExpressionConverterService.getMatchingExpression(r.matchType());
                        final String rule = format("${" + matchExpr + "}", r.field(), r.value());
                        return new CsResponse(responseName, description, rule);
                    }
                    return new CsResponse(responseName, description, null);
                }).collect(Collectors.toList());
    }

    private static Optional<CtMethod> getActionMethod(CtClass javaClass) {
        try {
            for (CtMethod m : javaClass.getMethods()) {
                if (m.hasAnnotation(Action.class)) {
                    return Optional.of(m);
                }
            }
        } catch (Exception ignored) {
        }
        return Optional.empty();
    }

    public static String getOperationDescription(CsOperation operation) {

        final String inputsDescription = operation.getInputs().stream()
                .filter(csInput -> !csInput.isPrivateField())
                .map(csInput -> {
                    final StringBuilder isInput = new StringBuilder(format("%s @input %s: ", COMMENT_CHAR, csInput.getName()));

                    final int indent = isInput.toString().length();
                    final String indentedDescription = wrapAndIndent(csInput.getDescription(), indent);

                    isInput.append(indentedDescription);

                    if (!csInput.isRequired()) {
                        isInput.append(NEW_LINE)
                                .append(COMMENT_CHAR)
                                .append(repeat(" ", indent - COMMENT_CHAR.length()))
                                .append("Optional");
                    }
                    return isInput.toString();
                })
                .collect(joining(NEW_LINE));

        final String outputsDescription = operation.getOutputs().stream()
                .map(csOutput -> ImmutablePair.of(csOutput.getDescription(), format("%s @output %s: ", COMMENT_CHAR, csOutput.getName())))
                .map(pair -> pair.getRight() + wrapAndIndent(pair.getLeft(), pair.getRight().length()))
                .collect(joining(NEW_LINE));

        final String responsesDescription = operation.getResults().stream()
                .map(csResponse -> ImmutablePair.of(csResponse.getDescription(), format("%s @result %s: ", COMMENT_CHAR, csResponse.getName())))
                .map(pair -> pair.getRight() + wrapAndIndent(pair.getLeft(), pair.getRight().length()))
                .collect(joining(NEW_LINE));

        return new StringBuilder(inputsDescription)
                .append(NEW_LINE)
                .append(COMMENT_CHAR)
                .append(NEW_LINE)
                .append(outputsDescription)
                .append(NEW_LINE)
                .append(COMMENT_CHAR)
                .append(NEW_LINE)
                .append(responsesDescription)
                .toString();
    }

    private static String wrapAndIndent(@NotNull final String description, final int indent) {
        final String wrappedDescription = wrap(description, MAX_LINE_LENGTH - indent);
        final String descriptionIndent = NEW_LINE + COMMENT_CHAR + repeat(" ", indent - COMMENT_CHAR.length());
        return replace(wrappedDescription, NEW_LINE, descriptionIndent);
    }
}
