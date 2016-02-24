# maven-usage

## Purpose

This is a command-line utility to allow scanning of a Maven repository for references to a specific artifact.

The user provides a group ID and artifact ID, and the utility scans the dependencies of each artifact in the repository, and identifies whether the supplied artifact is used, and reports on this.

## Config file

The utility is executed using the following command:

`java -jar maven-usage.jar --config=path to config file

This config file requires the following entries:

Entry | Purpose
------|---------
group=<groupname> | group to be searched for 
artifact=<artifactName> | artifact to be searched for 
searchDir=<path of directory to be searched> | directory to be searched - part of a local repo, or a specific directory within a local repo (to narrow the search) 
outputFormat=csv,html | output format(s) - comma-separated list
outputDirectory=<path of directory for results> | output directory - where the CSVs or reports will be written to
repo=<path of repo being searched> | Maven local repo

An sample config file is available in the config directory.

Logger tests from :http://bloodredsun.com/2014/06/03/checking-logback-based-logging-in-unit-tests/
