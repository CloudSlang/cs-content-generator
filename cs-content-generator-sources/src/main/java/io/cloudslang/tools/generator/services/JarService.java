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

package io.cloudslang.tools.generator.services;

import io.cloudslang.tools.generator.utils.PackageUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.apache.commons.io.FilenameUtils.getBaseName;
import static org.apache.commons.io.FilenameUtils.getExtension;

public class JarService {

    private static final String JAR = "jar";

    public static boolean isJar(Path jar) {
        return JAR.equalsIgnoreCase(getExtension(jar.toString()));
    }

    public static Path extractJar(Path jar) throws IOException {
        if (!isJar(jar)) {
            throw new IllegalArgumentException(String.format("%s is not a jar path", jar.toString()));
        }
        Path destination = Files.createTempDirectory(getBaseName(jar.toString()));
        ZipFile zipFile = new ZipFile(jar.toFile());
        Enumeration<? extends ZipEntry> jarArchive = zipFile.entries();
        while (jarArchive.hasMoreElements()) {
            ZipEntry zipEntry = jarArchive.nextElement();
            String name = zipEntry.getName();
            if (zipEntry.isDirectory()) {
                Path temp = destination.resolve(Paths.get(name));
                Files.createDirectories(temp);
            } else {
                InputStream is = zipFile.getInputStream(zipEntry);
                FileUtils.copyInputStreamToFile(is, destination.resolve(Paths.get(name)).toFile());
            }
        }
        return destination;
    }

    public static List<String> listClasses(Path jar) throws IOException {
        if (!isJar(jar)) {
            throw new IllegalArgumentException(String.format("%s is not a jar path", jar.toString()));
        }
        ZipFile zipFile = new ZipFile(jar.toFile());
        Enumeration<? extends ZipEntry> jarArchive = zipFile.entries();
        List<String> entries = new ArrayList<>();
        while (jarArchive.hasMoreElements()) {
            String nextName = jarArchive.nextElement().getName();
            if (getExtension(nextName).equalsIgnoreCase("class")) {
                entries.add(PackageUtils.getPackageFromPath(Paths.get(nextName)));
            }
        }
        return entries;
    }

    public static InputStream getPomFromJar(Path jar) throws IOException {
        if (!isJar(jar)) {
            throw new IllegalArgumentException(String.format("%s is not a jar path", jar.toString()));
        }
        ZipFile zipFile = new ZipFile(jar.toFile());
        Enumeration<? extends ZipEntry> jarArchive = zipFile.entries();
        while (jarArchive.hasMoreElements()) {
            ZipEntry nextName = jarArchive.nextElement();
            if (FilenameUtils.getName(nextName.getName()).equals("pom.xml")) {
                return zipFile.getInputStream(nextName);
            }
        }
        throw new IllegalArgumentException("Jar " + jar + "is not a Maven artifact");
    }
}
