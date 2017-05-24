package io.cloudslang.tools.cli;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

@Slf4j
public class Cli {
    private String[] args = null;
    private Options options = new Options();
    @Getter
    private String source;
    @Getter
    private String destination;

    public Cli(String[] args) {
        this.args = args;
        options = new Options();
        options.addOption("h", "help", false, "Help");
        options.addOption("s", "source", true, "The source file/folder where the XMLs are");
        options.addOption("d", "destination", true, "Where should the X -> Y");
    }

    public boolean parse() {
        final CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption("h"))
                help();

            if (cmd.hasOption("s")) {
                log.info("Using cli argument -s=" + cmd.getOptionValue("s"));
                source = cmd.getOptionValue("s");
            } else {
                log.error("Missing -s option");
                help();
            }

            if (cmd.hasOption("d")) {
                log.info("Using cli argument -d=" + cmd.getOptionValue("d"));
                destination = cmd.getOptionValue("d");
            } else {
                log.error("Missing -d option");
                help();
            }
            return true;
        } catch (ParseException e) {
            log.error("Failed to parse command line properties", e);
            help();
            return false;
        }
    }

    private void help() {
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Main", options);
        System.exit(0);
    }
}