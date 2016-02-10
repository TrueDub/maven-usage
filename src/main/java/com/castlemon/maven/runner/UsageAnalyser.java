package com.castlemon.maven.runner;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.SimpleCommandLinePropertySource;

import com.castlemon.maven.control.Controller;

public class UsageAnalyser {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsageAnalyser.class);

    private UsageAnalyser() {

    }

    public static void main(String[] args) {
        if (args.length != 5) {
            LOGGER.error("Incorrect parameters submitted to job");
            LOGGER.error(
                    "Usage: java -jar maven-usage.jar --group=<groupname> --artifact=<artifactName> --searchDir=<path of directory to be searched> --outputFormat=<xml,html> --outputDirectory=<path of directory for results>");
        } else {
            // set up Spring
            SimpleCommandLinePropertySource clps = new SimpleCommandLinePropertySource(args);
            assert clps.containsProperty("group") == true;
            assert clps.containsProperty("artifact") == true;
            assert clps.containsProperty("searchDir") == true;
            assert clps.containsProperty("outputFormat") == true;
            assert clps.containsProperty("outputDirectory") == true;
            AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
            applicationContext.getEnvironment().getPropertySources().addFirst(clps);
            applicationContext.register(Config.class);
            applicationContext.refresh();
            Controller controller = (Controller) applicationContext.getBean("controller");
            String group = clps.getProperty("group");
            String artifact = clps.getProperty("artifact");
            String searchDir = clps.getProperty("searchDir");
            List<String> outputFormat = Arrays.asList(clps.getProperty("outputFormat").split(","));
            String outputDirectory = clps.getProperty("outputDirectory");
            controller.executeAnalysis(group, artifact, searchDir, outputFormat, outputDirectory);
            applicationContext.close();
            LOGGER.info("Analysis run complete");
        }
    }

}
