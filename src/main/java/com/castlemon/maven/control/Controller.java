package com.castlemon.maven.control;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.castlemon.maven.exception.InvalidFileException;

@Component
public class Controller {

    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);

    public void executeAnalysis(String group, String artifact, String directoryName) {
        File directory = null;
        // get reference to directory
        try {
            directory = getDirectory(directoryName);
        } catch (FileNotFoundException e) {
            LOGGER.error("cannot find specified directory", e);
            return;
        } catch (InvalidFileException e) {
            LOGGER.error("specified file is not a directory", e);
            return;
        }
        // loop through subdirs and check poms
        List<String> results = processDirectory(directory);
        // write output
        outputResults(results);
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

    private List<String> processDirectory(File directory) {
        List<String> names = new ArrayList<String>();
        // find all files called *.pom in the specified directory and all subdirectories
        Collection<File> poms = FileUtils.listFiles(directory, new SuffixFileFilter(".pom"), TrueFileFilter.INSTANCE);
        for (File pom : poms) {
            names.add(pom.getAbsolutePath());
        }
        return names;
    }

    private void outputResults(List<String> results) {
        LOGGER.info("Results:");
        for (String result : results) {
            LOGGER.info(result);
        }
    }

}
