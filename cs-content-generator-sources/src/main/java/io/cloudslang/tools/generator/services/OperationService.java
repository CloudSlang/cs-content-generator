package io.cloudslang.tools.generator.services;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.tools.generator.entities.cs.*;
import io.cloudslang.tools.generator.services.converters.ResultExpressionConverterService;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.management.BadAttributeValueExpException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static io.cloudslang.tools.generator.utils.NameUtils.getResultName;
import static io.cloudslang.tools.generator.utils.NameUtils.toSnakeCase;
import static java.lang.System.lineSeparator;

public class OperationService {

    private static final List<String> SESSION_OBJECTS = Arrays.asList("GlobalSessionObject", "SerializableSessionObject");


    public static CsOperationFile getOperation(String gav, @NotNull CtClass javaClass) throws ClassNotFoundException, NotFoundException, BadAttributeValueExpException {
        CtMethod actionMethod = getActionMethod(javaClass);
        if (actionMethod != null) {
            Action action = (Action) actionMethod.getAnnotation(Action.class);
            final List<CsInput> inputs = getInputs(actionMethod);
            CsOperation operation = new CsOperation(
                    StringUtils.replace(toSnakeCase(action.name()), " ", "_"),
                    inputs,
                    getOutputs(action, inputs),
                    getAction(gav, javaClass, actionMethod),
                    getResponses(action));

            return new CsOperationFile(javaClass.getPackageName(), operation, getOperationDescription(operation));
        }
        return null;
    }

    private static CsJavaAction getAction(String gav, CtClass javaClass, CtMethod actionMethod) {
        return new CsJavaAction(gav, javaClass.getName(), actionMethod.getName()); //todo this is wrong, should be checked
//        return new CsJavaAction(gav, actionMethod.getName(), javaClass.getName());
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
                        inputs.add(new CsInput(name, param.required(), null, param.encrypted(), false));
                        if (!name.equals(param.value())) {
                            inputs.add(new CsInput(param.value(), false, String.format("${get(\'%s\', \'\')}", name), param.encrypted(), true));
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
            boolean hasInputWithSameName = inputs.stream().anyMatch(i -> i.getName().equals(outputNameCopy));
            if (hasInputWithSameName) {
                outputName += "_output";
            }
            String expression = String.format("${%s}", o.value());
            outputsList.add(new CsOutput(outputName, expression));
        }
        return outputsList;
    }

    private static List<CsResponse> getResponses(Action action) {
        List<CsResponse> responseList = new ArrayList<>();
        Response[] responses = action.responses();
        for (Response r : responses) {
            if (!r.isDefault()) {
                String matchExpr = ResultExpressionConverterService.getMatchingExpression(r.matchType());
                String rule = String.format("${" + matchExpr + "}", r.field(), r.value());
                responseList.add(new CsResponse(getResultName(r.text()), rule));
            } else {
                responseList.add(new CsResponse(getResultName(r.text()), null));
            }
        }
        return responseList;
    }

    private static CtMethod getActionMethod(CtClass javaClass) {
        for (CtMethod m : javaClass.getMethods()) {
            if (m.hasAnnotation(Action.class)) {
                return m;
            }
        }
        return null;
    }

    public static String getOperationDescription(CsOperation operation) {

        final String inputsDescription = operation.getInputs().stream()
                .filter(csInput -> !csInput.isPrivateField())
                .map(csInput -> {
                    final StringBuilder isInput = new StringBuilder(String.format("#! @input %s: ", csInput.getName()));

                    final int indent = isInput.toString().length() - 2;

                    isInput.append("Generated description");

                    if (!csInput.isRequired()) {
                        isInput.append(lineSeparator())
                                .append("#!")
                                .append(StringUtils.repeat(" ", indent))
                                .append("Optional");
                    }
                    return isInput.toString();
                })
                .collect(Collectors.joining(lineSeparator()));

        final String outputsDescription = operation.getOutputs().stream()
                .map(csOutput -> String.format("#! @output %s: Generated description", csOutput.getName()))
                .collect(Collectors.joining(lineSeparator()));

        final String responsesDescription = operation.getResults().stream()
                .map(csResponse -> String.format("#! @result %s: Generated description", csResponse.getName()))
                .collect(Collectors.joining(lineSeparator()));

        return new StringBuilder(inputsDescription)
                .append(lineSeparator())
                .append("#!")
                .append(lineSeparator())
                .append(outputsDescription)
                .append(lineSeparator())
                .append("#!")
                .append(lineSeparator())
                .append(responsesDescription)
                .toString();
    }
}
