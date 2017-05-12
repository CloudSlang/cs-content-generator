package io.cloudslang.tools.generator.services;

import io.cloudslang.tools.generator.entities.CliOptionValues;
import io.cloudslang.tools.generator.entities.OptionsEnum;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Author: Ligia Centea
 * Date: 4/5/2016.
 */
@Service
public class CliService {

    public static final String JAVA_COMMAND = "java -jar %s";
    private static final String HEADER = "Migrate content from AFL to CloudSlang";
    final Logger logger = LoggerFactory.getLogger(CliService.class);

    private OptionGroup buildComponentOptionsGroup() {
        Option help = Option.builder(OptionsEnum.HELP.getShortName())
                .longOpt(OptionsEnum.HELP.getLongName())
                .build();

        Option generateCS = Option.builder(OptionsEnum.GENERATE_CLOUDSLANG.getShortName())
                .longOpt(OptionsEnum.GENERATE_CLOUDSLANG.getLongName())
                .desc(OptionsEnum.GENERATE_CLOUDSLANG.getDescription())
                .build();

        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
        optionGroup.addOption(help);
        optionGroup.addOption(generateCS);

        return optionGroup;
    }

    private Options buildGenerateCloudSlangOptions() {
        Options options = new Options();

        Option destination = Option.builder(OptionsEnum.DESTINATION.getShortName())
                .longOpt(OptionsEnum.DESTINATION.getLongName())
                .required(true)
                .hasArg(true)
                .desc(OptionsEnum.DESTINATION.getDescription())
                .build();

        Option sourcesPath = Option.builder(OptionsEnum.SOURCES_PATH.getShortName())
                .longOpt(OptionsEnum.SOURCES_PATH.getLongName())
                .required(true)
                .hasArg(true)
                .desc(OptionsEnum.SOURCES_PATH.getDescription())
                .build();

        Option classFilter = Option.builder(OptionsEnum.FILTER.getShortName())
                .longOpt(OptionsEnum.FILTER.getLongName())
                .hasArg(true)
                .desc(OptionsEnum.FILTER.getDescription())
                .build();

        options.addOptionGroup(buildComponentOptionsGroup());
        options.addOption(destination);
        options.addOption(sourcesPath);
        options.addOption(classFilter);

        return options;
    }

    private Options buildGenerateActionsOptions() {
        Options options = new Options();

        Option destination = Option.builder(OptionsEnum.DESTINATION.getShortName())
                .longOpt(OptionsEnum.DESTINATION.getLongName())
                .required(true)
                .hasArg(true)
                .desc(OptionsEnum.DESTINATION.getDescription())
                .build();


        options.addOptionGroup(buildComponentOptionsGroup());
        options.addOption(destination);

        return options;
    }

    public CliOptionValues parse(String[] args) {
        CommandLineParser parser = new DefaultParser();
        CliOptionValues options = new CliOptionValues();

        try {
            CommandLine command = parser.parse(buildOptions(), args);
            if (command.hasOption(OptionsEnum.HELP.getShortName())) {
                options.setHelp(true);
            }  else {
                command = parser.parse(buildGenerateCloudSlangOptions(), args);
                options.setGenerateCloudSlang(true);
                options.setDestination(command.getOptionValue(OptionsEnum.DESTINATION.getShortName()));
                options.setJavaSource(command.getOptionValue(OptionsEnum.SOURCES_PATH.getShortName()));
                options.setClassFilter(command.getOptionValue(OptionsEnum.FILTER.getShortName()));
            }
        } catch (ParseException e) {
            if (!options.isHelp()) {
                logger.error("Invalid syntax: " + e);
                options.setHelp(true);
            }
        }
        return options;
    }

    public void printOptions() {
        String jarPath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        String command = String.format(JAVA_COMMAND, FilenameUtils.getBaseName(jarPath));

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(command, HEADER, buildOptions(), null, true);
    }

    private Options buildOptions() {
        Options result = new Options();
        result.addOptionGroup(buildComponentOptionsGroup());

        Options temp = buildGenerateActionsOptions();
        temp.getOptions().stream().forEach(o -> {
            o.setRequired(false);
            result.addOption(o);
        });
        temp = buildGenerateCloudSlangOptions();
        temp.getOptions().stream().forEach(o -> {
            o.setRequired(false);
            result.addOption(o);
        });

        return result;
    }
}
