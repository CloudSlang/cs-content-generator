/*
 * (c) Copyright 2017 Micro Focus, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
