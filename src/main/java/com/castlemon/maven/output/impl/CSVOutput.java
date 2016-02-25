package com.castlemon.maven.output.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.castlemon.maven.domain.RunData;
import com.castlemon.maven.domain.Usage;
import com.castlemon.maven.output.Output;
import com.opencsv.CSVWriter;

@Component
public class CSVOutput implements Output {

  private static final Logger LOGGER = LoggerFactory.getLogger(CSVOutput.class);

  public void writeData(RunData runData) {
    try {
      CSVWriter writer = new CSVWriter(new OutputStreamWriter(
          new FileOutputStream(runData.getOutputDirectory() + File.separator + "usage.csv"), "UTF-8"));
      writer.writeNext(Usage.getCSVTitles());
      for (Usage usage : runData.getUsages()) {
        writer.writeNext(usage.getCSVString());
      }
      writer.close();
    } catch (IOException e) {
      LOGGER.error("could not write CSV to " + runData.getOutputDirectory(), e);
    }
  }

}
