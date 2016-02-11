package com.castlemon.maven.control;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.maven.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.castlemon.maven.domain.Usage;
import com.castlemon.maven.exception.InvalidFileException;
import com.castlemon.maven.output.CSVOutput;
import com.castlemon.maven.output.HTMLOutput;
import com.castlemon.maven.processing.PomProcessor;

@Component
public class Controller {

    @Autowired
    private CSVOutput csvOutput;

    @Autowired
    private HTMLOutput htmlOutput;

    @Autowired
    private PomProcessor pomProcessor;

    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);

    public void executeAnalysis(String group, String artifact, String directoryName, List<String> outputFormats,
            String outputDirectory) {
        // get reference to directory
        File directory = null;
        try {
            directory = getDirectory(directoryName);
        } catch (FileNotFoundException e) {
            LOGGER.error("cannot find specified directory", e);
            return;
        } catch (InvalidFileException e) {
            LOGGER.error("specified file is not a directory", e);
            return;
        }

        // loop through subdirs and get poms
        Collection<File> results = processDirectory(directory);

        // pre-process the poms into models and place in a map
        Map<String, Model> models = pomProcessor.preProcessPoms(results);

        // process each model
        List<Usage> usages = new ArrayList<Usage>();
        for (Model pom : models.values()) {
            Usage usage = pomProcessor.processPomFile(pom, group, artifact);
            if (usage != null) {
                usages.add(usage);
            }
        }
        // sort usages
        Collections.sort(usages);
        // need to run through the usages, to check parent-supplied version numbers
        pomProcessor.processParentVersions(usages, models, group, artifact);

        // write output
        if (outputFormats.contains("csv")) {
            csvOutput.writeCSVFile(usages, outputDirectory);
        }
        if (outputFormats.contains("html")) {
            htmlOutput.writeHTMLOutput(group, artifact, directoryName, usages, outputDirectory);
        }
    }

    private File getDirectory(String directory) throws FileNotFoundException, InvalidFileException {
        File file = new File(directory);
        if (!file.exists()) {
            throw new FileNotFoundException();
        } else if (!file.isDirectory()) {
            throw new InvalidFileException(directory);
        }
        return file;
    }

    private Collection<File> processDirectory(File directory) {
        // find all files called *.pom in the specified directory and all subdirectories
        return FileUtils.listFiles(directory, new SuffixFileFilter(".pom"), TrueFileFilter.INSTANCE);
    }

}
