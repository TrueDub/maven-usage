package com.castlemon.maven.runner;

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
        if (args.length != 3) {
            LOGGER.error("Incorrect parameters submitted to job");
            LOGGER.error(
                    "Usage: java -jar maven-usage.jar --group=<groupname> --artifact=<artifactName> --searchDir=<path of directory to be searched>");
        } else {
            // set up Spring
            SimpleCommandLinePropertySource clps = new SimpleCommandLinePropertySource(args);
            assert clps.containsProperty("group") == true;
            assert clps.containsProperty("artifact") == true;
            assert clps.containsProperty("searchDir") == true;
            AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
            applicationContext.getEnvironment().getPropertySources().addFirst(clps);
            applicationContext.register(Config.class);
            applicationContext.refresh();
            Controller controller = (Controller) applicationContext.getBean("controller");
            String group = clps.getProperty("group");
            String artifact = clps.getProperty("artifact");
            String searchDir = clps.getProperty("searchDir");
            controller.executeAnalysis(group, artifact, searchDir);
            applicationContext.close();
            LOGGER.info("Analysis run complete");
        }
    }

}
