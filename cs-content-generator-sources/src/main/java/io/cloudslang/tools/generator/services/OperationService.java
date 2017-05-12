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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.management.BadAttributeValueExpException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static io.cloudslang.tools.generator.utils.NameUtils.getResultName;
import static io.cloudslang.tools.generator.utils.NameUtils.toSnakeCase;
import static java.lang.System.lineSeparator;

/**
 * Author: Ligia Centea
 * Date: 4/3/2016.
 */
@Service
public class OperationService {

    private static final List<String> SESSION_OBJECTS = Arrays.asList("GlobalSessionObject", "SerializableSessionObject");

    @Autowired
    private ResultExpressionConverterService matchTypeConverterService;

    public CsOperationFile getOperation(String gav, CtClass javaClass) throws ClassNotFoundException, NotFoundException, BadAttributeValueExpException {
        Assert.notNull(javaClass);
        CtMethod actionMethod = getActionMethod(javaClass);
        if (actionMethod != null) {
            CsOperation operation = new CsOperation();
            Action action = (Action) actionMethod.getAnnotation(Action.class);
            operation.setName(StringUtils.replace(toSnakeCase(action.name()), " ", "_"));
            operation.setInputs(getInputs(actionMethod));
            operation.setOutputs(getOutputs(action, operation.getInputs()));
            operation.setResults(getResponses(action));
            operation.setAction(getAction(gav, javaClass, actionMethod));

            CsOperationFile operationFile = new CsOperationFile();
            operationFile.setOperation(operation);
            operationFile.setNamespace(javaClass.getPackageName());
            return operationFile;
        }
        return null;
    }

    private CsJavaAction getAction(String gav, CtClass javaClass, CtMethod actionMethod) {
        return new CsJavaAction(gav, actionMethod.getName(), javaClass.getName());
    }

    private List<CsInput> getInputs(CtMethod actionMethod) throws ClassNotFoundException, NotFoundException, BadAttributeValueExpException {
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
                            inputs.add(new CsInput(param.value(), false, String.format("${get(\"%s\", \"\")}", name), param.encrypted(), true));
                        }
                    }
                }
            }
        }
        return inputs;
    }

    private List<CsOutput> getOutputs(Action action, List<CsInput> inputs) {
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

    private List<CsResponse> getResponses(Action action) {
        List<CsResponse> responseList = new ArrayList<>();
        Response[] responses = action.responses();
        for (Response r : responses) {
            if (!r.isDefault()) {
                String matchExpr = matchTypeConverterService.getMatchingExpression(r.matchType());
                String rule = String.format("${" + matchExpr + "}", r.field(), r.value());
                responseList.add(new CsResponse(getResultName(r.text()), rule));
            } else {
                responseList.add(new CsResponse(getResultName(r.text()), null));
            }
        }
        return responseList;
    }

    private CtMethod getActionMethod(CtClass javaClass) {
        for (CtMethod m : javaClass.getMethods()) {
            if (m.hasAnnotation(Action.class)) {
                return m;
            }
        }
        return null;
    }

    public String getOperationDescription(CsOperation operation) {
        StringBuilder description =
                new StringBuilder("########################################################################################################################" +
                        lineSeparator() + "#!" + lineSeparator() +
                        "#! @description: Generated operation description" + lineSeparator() +
                        "#!" + lineSeparator());

        final String inputsDescription = operation.getInputs().stream()
                .filter(csInput -> !csInput.isPrivateField())
                .map(csInput -> {
                    final StringBuilder isInput = new StringBuilder()
                            .append("#! @input ")
                            .append(csInput.getName())
                            .append(": ");

                    int indent = isInput.toString().length() - 2;

                    isInput.append("Generated description")
                            .append(lineSeparator());

                    if (csInput.isRequired()) {
                        isInput.append("#!");
                    } else {
                        isInput.append("#!")
                                .append(StringUtils.repeat(" ", indent))
                                .append("Optional");
                    }

                    return isInput.toString();
                })
                .collect(Collectors.joining("\n"));

        description.append(inputsDescription);

        description.append(lineSeparator());

        final String outputsDescription = operation.getOutputs().stream()
                .map(csOutput -> {
                    final StringBuilder isOutput = new StringBuilder()
                            .append("#! @output ")
                            .append(csOutput.getName())
                            .append(": ");

                    isOutput.append("Generated description");

                    return isOutput.toString();
                })
                .collect(Collectors.joining("\n"));

        description.append(outputsDescription);
        description.append(lineSeparator());
        description.append("#!");
        description.append(lineSeparator());

        for (CsResponse csResponse : operation.getResults()) {
            description.append("#! @result ")
                    .append(csResponse.getName())
                    .append(": Generated description")
                    .append(lineSeparator());
        }

        description.append("#!!#")
                .append(lineSeparator())
                .append("########################################################################################################################")
                .append(lineSeparator())
                .append(lineSeparator());
        return description.toString();
    }
}
