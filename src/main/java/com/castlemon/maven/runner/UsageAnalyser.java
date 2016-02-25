package com.castlemon.maven.runner;

import java.io.File;
import java.util.Arrays;

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
        // set up Spring
        SimpleCommandLinePropertySource clps = new SimpleCommandLinePropertySource(args);
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.getEnvironment().getPropertySources().addFirst(clps);
        applicationContext.register(Config.class);
        applicationContext.refresh();
        Controller controller = (Controller) applicationContext.getBean("controller");
        // options display required?
        if (clps.containsProperty("options") || clps.containsProperty("help")) {
            displayOptions();
        } else {
            RunData runData = readProperties(clps);
            if (runData != null) {
                controller.executeAnalysis(runData);
                LOGGER.info("Analysis run complete");
            } else {
                LOGGER.info("Analysis run halted with errors");
            }
        }
        applicationContext.close();
    }

    private static RunData readProperties(SimpleCommandLinePropertySource clps) {
        RunData runData = new RunData();
        if (clps.containsProperty("group")) {
            runData.setGroup(clps.getProperty("group"));
        } else {
            LOGGER.error("No group supplied");
            displayOptions();
            return null;
        }
        if (clps.containsProperty("artifact")) {
            runData.setArtifact(clps.getProperty("artifact"));
        } else {
            LOGGER.error("No artifact id supplied");
            displayOptions();
            return null;
        }
        if (clps.containsProperty("outputDir")) {
            runData.setOutputDirectory(clps.getProperty("outputDir"));
        } else {
            LOGGER.error("No output directory supplied");
            displayOptions();
            return null;
        }
        if (clps.containsProperty("searchDir")) {
            runData.setSearchDirectory(clps.getProperty("searchDir"));
        } else {
            runData.setSearchDirectory(getHomeDirRepo());
            LOGGER.info("setting search dir to " + runData.getSearchDirectory());
        }
        if (clps.containsProperty("outputFormats")) {
            runData.setOutputFormats(Arrays.asList(clps.getProperty("outputFormats").split(",")));
        } else {
            String[] defaults = { "HTML" };
            runData.setOutputFormats(Arrays.asList(defaults));
            LOGGER.info("default output format of HTML used");
        }
        if (clps.containsProperty("repo")) {
            runData.setSearchDirectory(clps.getProperty("repo"));
        } else {
            runData.setRepo(getHomeDirRepo());
            LOGGER.info("setting repo to " + runData.getRepo());
        }
        return runData;
    }

    private static String getHomeDirRepo() {
        String homeDir = System.getProperty("user.home");
        return homeDir + File.separator + ".m2" + File.separator + "repository";
    }

    private static void displayOptions() {
        LOGGER.info(" ");
        LOGGER.info("Possible options:");
        LOGGER.info(" ");
        LOGGER.info("--group=<groupId> - maven group of the artifact to be searched for - mandatory - no default");
        LOGGER.info(" ");
        LOGGER.info(
                "--artifact=<artifact> - maven artifact of the artifact to be searched for - mandatory - no default");
        LOGGER.info(" ");
        LOGGER.info("--outputDir=<outputDir> - directory where reports will be written to - mandatory - no default");
        LOGGER.info(" ");
        LOGGER.info(
                "--searchDir=<searchDir> - directory to be searched, might be the same as the repo, or a subdirectory of it - optional - default of ~/.m2/repository");
        LOGGER.info(" ");
        LOGGER.info(
                "--outputFormats=<outputFormat> - comma-separated list of output formats (CSV and HTML supported) - optional - default of HTML");
        LOGGER.info(" ");
        LOGGER.info("--repo=<repo> - location of maven repo - optional - default of ~/.m2/repository");
        LOGGER.info(" ");
    }

}
