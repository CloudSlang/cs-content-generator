package io.cloudslang.tools.generator.services;


import io.cloudslang.tools.generator.entities.cs.CsOperationFile;
import io.cloudslang.tools.generator.entities.dto.OperationFileDTO;
import io.cloudslang.tools.generator.services.converters.DTOConverterService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javassist.CtClass;
import javassist.NotFoundException;
import javax.management.BadAttributeValueExpException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Service;

/**
 * Author: Ligia Centea
 * Date: 4/26/2016.
 */
@Service
public class OperationGeneratorService {

    @Autowired
    private YamlSerializationService serializationService;

    @Autowired
    private DTOConverterService dtoConverterService;

    @Autowired
    private OperationService operationService;

    public Path generateCloudSlangWrapper(String gav, CtClass javaClass, Path destination) throws IOException, ParseException, ClassNotFoundException, NotFoundException, BadAttributeValueExpException {
        CsOperationFile operation = operationService.getOperation(gav, javaClass);
        if (operation != null) {
            String operationDescription = operationService.getOperationDescription(operation.getOperation());
            OperationFileDTO operationFileDTO = dtoConverterService.getOperationFileDTO(operation);
            Path result = destination.resolve(StringUtils.replace(operation.getNamespace(), ".", destination.getFileSystem().getSeparator()));
            if (!Files.exists(result)) {
                Files.createDirectories(result);
            }
            result = Paths.get(result.toString(), operation.getOperation().getName() + ".sl");
            String serializedOperation = serializationService.serialize(operationFileDTO, result);
            FileUtils.write(result.toFile(), operationDescription);
            FileUtils.write(result.toFile(), serializedOperation, true);
            return result;
        }
        return null;
    }
}
