package io.cloudslang.tools.generator.services;

import com.github.javaparser.ParseException;
import io.cloudslang.tools.generator.services.fs.JarService;
import io.cloudslang.tools.generator.services.fs.MavenService;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javax.management.BadAttributeValueExpException;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.model.building.ModelBuildingException;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

/**
 * Author: Ligia Centea
 * Date: 4/5/2016.
 */
@Service("csWrapper")
public class CloudSlangGeneratorService {

    final Logger logger = LoggerFactory.getLogger(CloudSlangGeneratorService.class);

    @Autowired
    private OperationGeneratorService generatorService;

    @Autowired
    private JarService jarService;

    @Autowired
    private ClassPool classPool;

    @Autowired
    private MavenService mavenService;

    public void generateWrapper(String jarPath, String className, String destination) {
        Path destPath = Paths.get(destination);
        try {
            classPool.appendClassPath(jarPath);
            String gav = mavenService.findArtifactGav(Paths.get(jarPath));

            List<String> classes = selectActionClasses(jarPath, className);
            for (String cls : classes) {
                try {
                    // TODO collect failures in an object if necessary
                    CtClass ctClass = classPool.getCtClass(cls);
                    Path result = generatorService.generateCloudSlangWrapper(gav, ctClass, destPath);
                    if (result != null) {
                        logger.info("Generated CloudSlang operation for class " + ctClass.getName() +
                                " at " + result);
                    }
                } catch (NotFoundException | ClassNotFoundException | BadAttributeValueExpException e) {
                    logger.error("Error occurred while generating CloudSlang for class " + cls + ":" + getStackTrace(e));
                }
            }
        } catch (NotFoundException | ModelBuildingException | XmlPullParserException | IOException e) {
            logger.error("Error occurred while generating CloudSlang: " + getStackTrace(e));
        }
    }

    private List<String> selectActionClasses(String jarPath, String className) throws IOException {
        List<String> classes;
        if (StringUtils.isEmpty(className)) {
            classes = jarService.listClasses(Paths.get(jarPath));
        } else {
            classes = new ArrayList<>();
            classes.add(className);
        }
        return classes;
    }
}
