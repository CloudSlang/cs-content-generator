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
                final Path sourcePath = Paths.get(cliParser.getSource());
                final Path destinationPath = Paths.get(cliParser.getDestination());
                if (Files.exists(sourcePath) && StringUtils.isNotBlank(cliParser.getDestination())) {
//                    CsGenerator(sourcePath, destinationPath);
                }
            } else {
                log.error("Please try again");
            }
        }
}
