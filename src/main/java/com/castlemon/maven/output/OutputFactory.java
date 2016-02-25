package com.castlemon.maven.output;

import org.springframework.stereotype.Component;

import com.castlemon.maven.output.impl.CSVOutput;
import com.castlemon.maven.output.impl.HTMLOutput;

@Component
public class OutputFactory {

  public Output getOutput(String outputType) {
    if (outputType.equalsIgnoreCase("CSV")) {
      return new CSVOutput();
    } else if (outputType.equalsIgnoreCase("HTML")) {
      return new HTMLOutput();
    } else {
      return null;
    }
  }

}
