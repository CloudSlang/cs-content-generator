package io.cloudslang.tools.generator;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.github.jknack.handlebars.EscapingStrategy;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.FileTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.tools.generator.entities.CsOperationFile;
import io.cloudslang.tools.generator.services.JarService;
import io.cloudslang.tools.generator.services.MavenService;
import io.cloudslang.tools.generator.services.OperationService;
import io.cloudslang.tools.generator.utils.YamlUtils;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jetbrains.annotations.NotNull;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

@Slf4j
public class CsGenerator {

    public static final String OPERATION_JAVA_ACTION_GAV = "operation.java_action.gav";
    public static final String DELIMITER = ".";
    private final ClassPool classPool;

    public CsGenerator() {
        classPool = ClassPool.getDefault();
        classPool.appendClassPath(new ClassClassPath(Action.class));
        classPool.appendClassPath(new ClassClassPath(Param.class));
        classPool.appendClassPath(new ClassClassPath(Output.class));
        classPool.appendClassPath(new ClassClassPath(Response.class));
    }

    public static Optional<Template> loadTemplate(@NotNull final String pathToTemplate) {
        final TemplateLoader templateLoader = new FileTemplateLoader("", "");
        final Handlebars handlebars = new Handlebars(templateLoader).with(EscapingStrategy.NOOP);
        try {
            return Optional.of(handlebars.compileInline(getStringTemplate(pathToTemplate)));
        } catch (IOException e) {
            log.error(ExceptionUtils.getStackTrace(e));
            return Optional.empty();
        }
    }

    @SneakyThrows
    private static String getStringTemplate(@NotNull final String pathToTemplate) {
        final InputStream inputStream = ClassLoader.getSystemResourceAsStream(pathToTemplate);
        return IOUtils.toString(inputStream, UTF_8);
    }


    private Optional<Path> generateNewCloudSlangWrapper(final Template template,
                                                        final CsOperationFile operation,
                                                        final Path slFile) {
        if (!Files.exists(slFile.getParent())) {
            try {
                Files.createDirectories(slFile.getParent());
            } catch (IOException e) {
                log.error("Could not create parent directory " + slFile.getParent().toString(), e);
            }
        }
        try (final Writer writer = Files.newBufferedWriter(slFile, UTF_8, StandardOpenOption.CREATE)) {
            template.apply(operation.toMap(), writer);
            writer.flush();
        } catch (IOException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }

        return Optional.of(slFile);
    }


    private Optional<Path> generateCloudSlangWrapper(final Template template, String gav, CtClass javaClass, Path destination) throws Exception {
        CsOperationFile operation = OperationService.getOperation(gav, javaClass);
        if (operation != null) {
            final Path parentDir = destination.resolve(StringUtils.replace(operation.getNamespace(), ".", destination.getFileSystem().getSeparator()));
            final Path slFile = parentDir.resolve(operation.getOperation().getName() + ".sl");
            if (Files.exists(slFile) /* && wantsToUpdate */) {
                final Optional<Path> updatedFileOpt = updateCloudSlangWrapper(operation, slFile);
                updatedFileOpt.ifPresent(updatedFile -> log.info("Updated file " + updatedFile.toString()));
            } else {
                final Optional<Path> generatedFileOpt = generateNewCloudSlangWrapper(template, operation, slFile);
                generatedFileOpt.ifPresent(path -> log.info("Generated CloudSlang operation for class " + javaClass.getName() + " at " + path));
            }
        }
        return Optional.empty();
    }

    private Optional<Path> updateCloudSlangWrapper(final CsOperationFile operation, final Path slFile) {
        try {
            final YamlReader reader = new YamlReader(new FileReader(slFile.toString()));
            final Object parsed = reader.read();

            final Optional<Object> oldGavOpt = YamlUtils.simpleYPath(parsed, OPERATION_JAVA_ACTION_GAV, DELIMITER);

            if (oldGavOpt.isPresent()) {
                final String oldGav = (String) oldGavOpt.get();

                final String newGav = operation.getOperation().getAction().getGav();

                if (!oldGav.equalsIgnoreCase(newGav)) {
                    final String slFileContent = FileUtils.readFileToString(slFile.toFile(), UTF_8);

                    final String updatedContent = StringUtils.replace(slFileContent, oldGav, operation.getOperation().getAction().getGav());

                    FileUtils.write(slFile.toFile(), updatedContent, UTF_8);

                    return Optional.of(slFile);
                }
            }
        } catch (IOException e) {
            log.error("Unable to open file.", e);
        }
        return Optional.empty();
    }


    public void generateWrapper(Path jarPath, String className, Path destPath) {
        try {
            classPool.appendClassPath(jarPath.toString());
            String gav = MavenService.findArtifactGav(jarPath);

            final List<String> classes = selectActionClasses(jarPath, className);
            final Optional<Template> optionalOperationTemplate = CsGenerator.loadTemplate("templates/CloudSlangOperation.hbs");
            log.info("Template loaded");
            optionalOperationTemplate.ifPresent(operationTemplate ->
                    classes.parallelStream()
                            .forEach(cls -> {
                                try {
                                    final CtClass ctClass = classPool.getCtClass(cls);
                                    generateCloudSlangWrapper(operationTemplate, gav, ctClass, destPath);
                                } catch (Exception e) {
                                    log.error("Error occurred while generating CloudSlang for class " + cls + ":" + getStackTrace(e));
                                }
                            }));
        } catch (Exception e) {
            log.error("Error occurred while generating CloudSlang: " + getStackTrace(e));
        }
    }

    private List<String> selectActionClasses(Path jarPath, String className) throws IOException {
        if (StringUtils.isEmpty(className)) {
            return JarService.listClasses(jarPath);
        }
        return Collections.singletonList(className);
    }
}
