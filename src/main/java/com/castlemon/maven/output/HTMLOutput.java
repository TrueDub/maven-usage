package com.castlemon.maven.output;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.castlemon.maven.domain.Usage;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Component
public class HTMLOutput {

    private static final Logger LOGGER = LoggerFactory.getLogger(HTMLOutput.class);

    public void writeHTMLOutput(String group, String artifact, String directoryName, List<Usage> usages,
            String outputDir) {
        try {
            setupResources(outputDir);
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
            cfg.setClassForTemplateLoading(this.getClass(), "/templates");
            cfg.setDefaultEncoding("UTF-8");
            Map<String, Object> context = new HashMap<String, Object>();
            context.put("group", group);
            context.put("artifact", artifact);
            context.put("directoryName", directoryName);
            context.put("usages", usages);
            Template template = cfg.getTemplate("usagereport.ftl");
            File file = new File(outputDir + File.separator + "usagereport.html");
            Writer out = new OutputStreamWriter(new FileOutputStream(file));
            template.process(context, out);
        } catch (IOException e) {
            LOGGER.error("I/O Error when writing HTML file", e);
        } catch (TemplateException e) {
            LOGGER.error("Template error when writing HTML file", e);
        }
    }

    private void setupResources(String outputDir) {
        File cssDir = new File(outputDir + File.separator + "css");
        cssDir.mkdir();
        InputStream resourceArchiveInputStream = null;
        FileOutputStream cssOutStream = null;
        try {
            resourceArchiveInputStream = HTMLOutput.class.getResourceAsStream("css/bootstrap.min.css");
            if (resourceArchiveInputStream == null) {
                resourceArchiveInputStream = HTMLOutput.class.getResourceAsStream("/css/bootstrap.min.css");
            }
            File file = new File(cssDir, "bootstrap.min.css");
            cssOutStream = new FileOutputStream(file);
            IOUtils.copy(resourceArchiveInputStream, cssOutStream);
        } catch (FileNotFoundException e) {
            LOGGER.error("Cannot find css file", e);
        } catch (IOException e) {
            LOGGER.error("Error accessing css file", e);
        } finally {
            IOUtils.closeQuietly(resourceArchiveInputStream);
            IOUtils.closeQuietly(cssOutStream);
        }
    }

}
