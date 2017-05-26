<a href="http://cloudslang.io/">
    <img src="https://camo.githubusercontent.com/ece898cfb3a9cc55353e7ab5d9014cc314af0234/687474703a2f2f692e696d6775722e636f6d2f696849353630562e706e67" alt="CloudSlang logo" title="CloudSlang" align="right" height="60"/>
</a>

CloudSlang Content Generator
============================

[![Join the chat at https://gitter.im/CloudSlang/cs-content-generator](https://badges.gitter.im/CloudSlang/cs-content-generator.svg)](https://gitter.im/CloudSlang/cs-content-generator?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

[![Build Status](https://travis-ci.org/CloudSlang/cs-content-generator.svg?branch=master)](https://travis-ci.org/CloudSlang/cs-content-generator)


This repository contains a tool that generates CloudSlang .sl files based on Java Actions.

1. [Description](#description)
2. [General Usage](#general-usage)
3. [Contribution Guideline](contribution-guideline)

<a name="description"/>

## Description

This tool will offer the possibility to generate Java Actions from the project [Cloudslang/cs-actions](https://github.com/CloudSlang/cs-actions)
and generate the .sl files that will be added in [Cloudslang/cloud-slang-content](https://github.com/CloudSlang/cloud-slang-content)

<a name="general-usage"/>

## General usage

To run the tool:

    java -jar cs-content-generator-<version>.jar -cs [-s <arg>] [-d <arg>]

     -s, --source <arg>           The absolute path of the .jar file that contains @Actions for which CloudSlang operations 
                              will be generated.
     -d, --destination <arg>   The absolute path of the CloudSlang sources.
     -h, --help
   
   
<a name="contribution-guideline"/>                                       
                                       
## Contribution Guideline
                                       
Read our Contribution Guide [here](CONTRIBUTING.md).                                       
                              
                              



