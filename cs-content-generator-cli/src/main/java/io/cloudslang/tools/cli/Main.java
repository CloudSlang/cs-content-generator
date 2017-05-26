package io.cloudslang.tools.cli;

import io.cloudslang.tools.generator.CsGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class Main {
        public static void main(String[] args) {
            final Cli cliParser = new Cli(args);
            if (cliParser.parse()) {
                final Path jarPath = Paths.get(cliParser.getSource());
                final Path destination = Paths.get(cliParser.getDestination());
                if (Files.exists(jarPath) && StringUtils.isNotBlank(cliParser.getDestination())) {
                    CsGenerator csGenerator = new CsGenerator();
                    csGenerator.generateWrapper(jarPath, "", destination);
                }
            } else {
                log.error("Please try again");
            }
        }
}
