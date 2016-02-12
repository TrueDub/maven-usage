package com.castlemon.maven.control;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.castlemon.maven.domain.RunData;
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

    public void executeAnalysis(RunData runData) {
        Date startTime = new Date();
        // get reference to directory
        File directory = null;
        try {
            directory = getDirectory(runData.getSearchDirectory());
        } catch (FileNotFoundException e) {
            LOGGER.error("cannot find specified directory", e);
            return;
        } catch (InvalidFileException e) {
            LOGGER.error("specified file is not a directory", e);
            return;
        }

        // loop through subdirs and get poms
        Collection<File> results = processDirectory(directory);
        runData.setPomsRead(results.size());

        // process the poms into models
        pomProcessor.processPoms(runData, results);

        // remove nulls
        runData.getUsages().removeAll(Collections.singleton(null));

        // sort usages
        Collections.sort(runData.getUsages());

        // calculcate timing
        Date endTime = new Date();
        long elapsedTime = endTime.getTime() - startTime.getTime();
        runData.setExecutionTimeInMillis(elapsedTime);

        // write output
        if (runData.getOutputFormats().contains("csv")) {
            csvOutput.writeCSVFile(runData);
        }
        if (runData.getOutputFormats().contains("html")) {
            htmlOutput.writeHTMLOutput(runData);
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
