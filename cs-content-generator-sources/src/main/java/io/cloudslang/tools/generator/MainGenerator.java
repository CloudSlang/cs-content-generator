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

package io.cloudslang.tools.generator;

import java.nio.file.Path;
import java.nio.file.Paths;

public class MainGenerator {
    public static final Path jarPath = Paths.get(System.getProperty("user.home") + "\\Desktop\\cs-azure-0.0.6-SNAPSHOT.jar");
    public static final String className = "";
    public static final Path destination = Paths.get(System.getProperty("user.home") + "\\Desktop\\potato");

    public static void main(String[] args) {
        CsGenerator csGenerator = new CsGenerator();
        csGenerator.generateWrapper(jarPath, className, destination);
    }
}