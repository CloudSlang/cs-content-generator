package io.cloudslang.tools.generator.services;

import io.cloudslang.tools.generator.entities.CliOptionValues;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Author: Ligia Centea
 * Date: 4/18/2016.
 */
@Service("migrationService")
public class MigrationService {

    final Logger logger = LoggerFactory.getLogger(MigrationService.class);

    @Autowired
    private CliService cliService;

    @Autowired
    private CloudSlangGeneratorService cloudSlangGeneratorService;

    public void migrate(String[] args) {
        CliOptionValues options = cliService.parse(args);

        if (options.isHelp()) {
            cliService.printOptions();
        } else {
            cloudSlangGeneratorService.generateWrapper(options.getJavaSource(),
                    options.getClassFilter(),
                    options.getDestination());
        }
    }

}
