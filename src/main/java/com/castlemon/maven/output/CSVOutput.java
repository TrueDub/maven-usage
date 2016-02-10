package com.castlemon.maven.output;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.castlemon.maven.domain.Usage;
import com.castlemon.maven.runner.UsageAnalyser;
import com.opencsv.CSVWriter;

@Component
public class CSVOutput {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsageAnalyser.class);

    String[] titles = { "Group", "Artifact", "Version", "Parent Group", "Parent Artifact", "Version Used", "Classifier",
            "Scope" };

    public void writeCSVFile(List<Usage> usages, String outputDir) {
        try {
            CSVWriter writer = new CSVWriter(
                    new OutputStreamWriter(new FileOutputStream(outputDir + File.separator + "usage.csv"), "UTF-8"));
            writer.writeNext(titles);
            for (Usage usage : usages) {
                writer.writeNext(usage.getCSVString());
            }
            writer.close();
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("encoding problem writing CSV to " + outputDir, e);
        } catch (FileNotFoundException e) {
            LOGGER.error("could not find " + outputDir, e);
        } catch (IOException e) {
            LOGGER.error("could not write CSV to " + outputDir, e);
        }
    }

}
