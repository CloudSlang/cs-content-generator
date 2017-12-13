/*
 * Copyright © 2017 EntIT Software LLC, a Micro Focus company (L.P.)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
        options.addOption("s", "source", true, "The source file/folder of the Actions that will be converted.");
        options.addOption("d", "destination", true, "Absolute path to the location where the .sl files will be created.");
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
        formatter.printHelp("MainGenerator", options);
        System.exit(0);
    }
}