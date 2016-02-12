package com.castlemon.maven.output;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
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

    public void writeHTMLOutput(String group, String artifact, String directoryName, Collection<Usage> usages,
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
        setupCSSResources(outputDir);
        setupJSResources(outputDir);
        setupFontResources(outputDir);
    }

    private void setupCSSResources(String outputDir) {
        File cssDir = makeDir("css", outputDir);
        copyFile(cssDir, "bootstrap.min.css", "css");
        copyFile(cssDir, "dataTables.bootstrap.min.css", "css");
    }

    private void setupJSResources(String outputDir) {
        File jsDir = makeDir("js", outputDir);
        copyFile(jsDir, "dataTables.bootstrap.min.js", "js");
        copyFile(jsDir, "jquery-1.12.0.min.js", "js");
        copyFile(jsDir, "jquery.dataTables.min.js", "js");
    }

    private void setupFontResources(String outputDir) {
        File jsDir = makeDir("fonts", outputDir);
        copyFile(jsDir, "glyphicons-halflings-regular.woff2", "fonts");
        copyFile(jsDir, "glyphicons-halflings-regular.woff", "fonts");
        copyFile(jsDir, "glyphicons-halflings-regular.ttf", "fonts");
    }

    private void copyFile(File cssDir, String fileName, String resourceDir) {
        InputStream resourceArchiveInputStream = null;
        FileOutputStream outStream = null;
        String filePath = resourceDir + "/" + fileName;
        try {
            resourceArchiveInputStream = HTMLOutput.class.getResourceAsStream(filePath);
            if (resourceArchiveInputStream == null) {
                resourceArchiveInputStream = HTMLOutput.class.getResourceAsStream("/" + filePath);
            }
            File file = new File(cssDir, fileName);
            outStream = new FileOutputStream(file);
            IOUtils.copy(resourceArchiveInputStream, outStream);
        } catch (FileNotFoundException e) {
            LOGGER.error("Cannot find file " + filePath, e);
        } catch (IOException e) {
            LOGGER.error("Error accessing file " + filePath, e);
        } finally {
            IOUtils.closeQuietly(resourceArchiveInputStream);
            IOUtils.closeQuietly(outStream);
        }
    }

    private File makeDir(String dirName, String outputDir) {
        File newDir = new File(outputDir + File.separator + dirName);
        newDir.mkdir();
        return newDir;
    }

}
