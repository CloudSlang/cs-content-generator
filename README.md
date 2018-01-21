<a href="http://cloudslang.io/">
    <img src="https://camo.githubusercontent.com/ece898cfb3a9cc55353e7ab5d9014cc314af0234/687474703a2f2f692e696d6775722e636f6d2f696849353630562e706e67" alt="CloudSlang logo" title="CloudSlang" align="right" height="60"/>
</a>

CloudSlang Content Generator
============================

[![Join the chat at https://gitter.im/CloudSlang/cs-content-generator](https://badges.gitter.im/CloudSlang/cs-content-generator.svg)](https://gitter.im/CloudSlang/cs-content-generator?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

[![Build Status](https://travis-ci.org/CloudSlang/cs-content-generator.svg?branch=master)](https://travis-ci.org/CloudSlang/cs-content-generator)


This repository contains a tool that generates / updates CloudSlang .sl files based on Java Actions.

1. [Description](#description)
2. [General Usage](#general-usage)
3. [Contribution Guideline](#contribution-guideline)

<a name="description"/>

## Description

This tool will offer the possibility to convert Java Actions from the project [Cloudslang/cs-actions](https://github.com/CloudSlang/cs-actions)
and generate/update the .sl files that will be added in [Cloudslang/cloud-slang-content](https://github.com/CloudSlang/cloud-slang-content)

<a name="general-usage"/>

## General usage

To run the tool:

#### PREREQUISITES

> Java 8 installed in order to run the tool.

> [Maven 3.3.9](https://archive.apache.org/dist/maven/maven-3/3.3.9/binaries/) installed in order to build the tool. 

1. Clone this repository and build it, by running (**mvn clean install**).
   Copy 'cs-content-generator-\<version>.jar' to a folder of your choice.

2. Download from [Maven Central](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22io.cloudslang.content%22) any of the packages 
   available in [Cloudslang/cs-actions](https://github.com/CloudSlang/cs-actions) 
   or clone the repository, build locally using Maven 3.3.9 and copy cs-packageName-0.0.*.jar to a location from which
   it will be used as the --source argument. The path can be, either relative or absolute.
   
   ```
   Example:
   -s C:\Users\yourUsername\.m2\repository\io\cloudslang\content\cs-ssh-0.0.36\cs-ssh-0.0.36.jar
   -s cs-ssh-0.0.38.jar - If the package is in the same folder as the tool.
   
   -d C:\Users\yourUsername\cloudslang\cloud-slang-content\content
   -d content - If you want to save the generated content in the same folder as the tool.
   ```
   
3. Open command prompt in Windows or a terminal in Linux, go to the location where cs-content-generator-<version>.jar 
   is located and run the following command:

    ```
    java -jar cs-content-generator-<version>.jar -s javaPackage -d folderName 

     -s, --source       The source file/folder of the Actions that will be converted.
     -d, --destination  Absolute path to the location where the .sl files will be created.
     -h, --help
    ```

4. Copy the newly created .sl files to your CloudSlang content folder or set -d **<path_to>\..\cloud-slang-content\content**.

> Note: If the files already exist, they will be updated with the new values.
        At the moment, only the gav: section is updated.
        
NEW: cs-content-generator-plugin 

This plugin can also be added as another step in a Maven build to generate content.

Maven plugin example:
```
    [...]
      <plugins>
            <plugin>
                <groupId>io.cloudslang.tools</groupId>
                <artifactId>cs-content-generator-plugin</artifactId>
                <version>${version}</version>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>generate</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
      </plugins>
    [...]
``` 

The plugin can run independent from the build: 'mvn package' which will generate the .sl files as long as there is a 
.jar in the ${project.build.directory} of the project.
   
<a name="contribution-guideline"/>                                       
                                       
## Contribution Guideline
                                       
Read our Contribution Guide [here](CONTRIBUTING.md).                                       
                              