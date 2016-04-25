package com.castlemon.maven.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.beust.jcommander.JCommander;
import com.castlemon.maven.control.Controller;
import com.castlemon.maven.domain.RunData;

public class UsageAnalyser {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsageAnalyser.class);

    private UsageAnalyser() {

    }

    public static void main(String[] args) {
        // set up Spring
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(Config.class);
        applicationContext.refresh();
        Controller controller = (Controller) applicationContext.getBean("controller");
        RunData runData = new RunData();
        JCommander jc = new JCommander(runData);
        jc.setDefaultProvider(new DefaultProvider());
        jc.setProgramName("UsageAnalyser");
        jc.parse(args);
        if (runData.displayHelp()) {
            StringBuilder builder = new StringBuilder();
            jc.usage(builder);
            LOGGER.info(builder.toString());
        } else {
            controller.executeAnalysis(runData);
            LOGGER.info("Analysis run complete");
        }
        applicationContext.close();
    }

}
