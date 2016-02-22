package com.castlemon.maven.runner;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.SimpleCommandLinePropertySource;

import com.castlemon.maven.control.Controller;
import com.castlemon.maven.domain.RunData;

public class UsageAnalyser {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsageAnalyser.class);

    private UsageAnalyser() {

    }

    public static void main(String[] args) {
        if (args.length != 1) {
            LOGGER.error("Incorrect parameters submitted to job");
            LOGGER.error("Usage: java -jar maven-usage.jar --config=<path to config file>");
        } else {
            // set up Spring
            SimpleCommandLinePropertySource clps = new SimpleCommandLinePropertySource(args);
            AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
            applicationContext.getEnvironment().getPropertySources().addFirst(clps);
            applicationContext.register(Config.class);
            applicationContext.refresh();
            Controller controller = (Controller) applicationContext.getBean("controller");
            RunData runData = readProperties(clps.getProperty("config"));
            if (runData != null) {
                controller.executeAnalysis(runData);
            }
            applicationContext.close();
            LOGGER.info("Analysis run complete");
        }
    }

    private static RunData readProperties(String configFileName) {
        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream(configFileName);
            prop.load(input);
        } catch (IOException e) {
            LOGGER.error("Cannot load config file - " + configFileName, e);
            return null;
        }
        RunData runData = new RunData();
        runData.setGroup(prop.getProperty("group"));
        runData.setArtifact(prop.getProperty("artifact"));
        runData.setSearchDirectory(prop.getProperty("searchDir"));
        runData.setOutputFormats(Arrays.asList(prop.getProperty("outputFormat").split(",")));
        runData.setOutputDirectory(prop.getProperty("outputDirectory"));
        runData.setRepo(prop.getProperty("repo"));
        return runData;
    }

}
