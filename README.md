# maven-usage

## Purpose

This is a command-line utility to allow scanning of a Maven repository for references to a specific artifact.

The user provides a group ID and artifact ID, and the utility scans the dependencies of each artifact in the repository, and identifies whether the supplied artifact is used, and reports on this.

## Command-line parameters

The utility is executed using the following command:

```
java -jar maven-usage.jar list-of-params
```

Executing the utility with either -options or -help will give you a list of parameters and their defaults, if any.

The available params are:

Entry | Purpose | Mandatory? | Default 
------|---------|------------|---------
-group= | group to be searched for | Yes | none
-artifact= | artifact to be searched for | Yes | none
-outputDir= | output directory - where the CSVs or reports will be written to | Yes | none
-searchDir= | directory to be searched - part of a local repo, or a specific directory within a local repo (to narrow the search)  | No | ~/.m2/repository
-outputFormats= | output format(s) - a comma-separated list of formats (currently HTML and CSV supported)  | No | HTML
-repo= | Maven local repo  | No | ~/.m2/repository

## License

This code is distributed under the MIT license - please see LICENSE.md for details.

## Acknowledgements

The code to test logging messages was adapted from the code supplied on http://bloodredsun.com/2014/06/03/checking-logback-based-logging-in-unit-tests/.
