package io.cloudslang.tools.generator;

import com.github.jknack.handlebars.EscapingStrategy;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.FileTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.tools.generator.entities.cs.CsOperationFile;
import io.cloudslang.tools.generator.services.OperationService;
import io.cloudslang.tools.generator.services.fs.JarService;
import io.cloudslang.tools.generator.services.fs.MavenService;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

@Slf4j
public class CsGenerator {

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

    private Optional<Path> generateCloudSlangWrapper(final Template template, String gav, CtClass javaClass, Path destination) throws Exception {
        CsOperationFile operation = OperationService.getOperation(gav, javaClass);
        if (operation != null) {
            Path result = destination.resolve(StringUtils.replace(operation.getNamespace(), ".", destination.getFileSystem().getSeparator()));
            if (!Files.exists(result)) {
                Files.createDirectories(result);
            }
            result = Paths.get(result.toString(), operation.getOperation().getName() + ".sl");
            try (final Writer writer = Files.newBufferedWriter(result, StandardCharsets.UTF_8, StandardOpenOption.CREATE)) {
                template.apply(operation.toMap(), writer);
                writer.flush();
            } catch (IOException e) {
                log.error(ExceptionUtils.getStackTrace(e));
            }

            return Optional.of(result);
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
                                    final Optional<Path> optResult = generateCloudSlangWrapper(operationTemplate, gav, ctClass, destPath);
                                    optResult.ifPresent(path -> log.info("Generated CloudSlang operation for class " + ctClass.getName() + " at " + path));
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
