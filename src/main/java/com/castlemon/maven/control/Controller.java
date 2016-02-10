package com.castlemon.maven.control;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.castlemon.maven.domain.Usage;
import com.castlemon.maven.exception.InvalidFileException;
import com.castlemon.maven.output.CSVOutput;

@Component
public class Controller {

    @Autowired
    private CSVOutput csvOutput;

    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);

    public void executeAnalysis(String group, String artifact, String directoryName, List<String> outputFormats,
            String outputDirectory) {
        File directory = null;
        // get reference to directory
        try {
            directory = getDirectory(directoryName);
        } catch (FileNotFoundException e) {
            LOGGER.error("cannot find specified directory", e);
            return;
        } catch (InvalidFileException e) {
            LOGGER.error("specified file is not a directory", e);
            return;
        }
        // loop through subdirs and check poms
        Collection<File> results = processDirectory(directory);
        List<Usage> usages = new ArrayList<Usage>();
        for (File pom : results) {
            usages.add(processPomFile(pom, group, artifact));
        }
        // remove nulls
        usages.removeAll(Collections.singleton(null));
        // write output
        outputCSV(usages);
        if (outputFormats.contains("csv")) {
            csvOutput.writeCSVFile(usages, outputDirectory);
        }
    }

    private File getDirectory(String directory) throws FileNotFoundException, InvalidFileException {
        File file = new File(directory);
        if (!file.exists()) {
            throw new FileNotFoundException();
        } else if (!file.isDirectory()) {
            throw new InvalidFileException(directory);
        }
        return file;
    }

    private Collection<File> processDirectory(File directory) {
        // find all files called *.pom in the specified directory and all subdirectories
        return FileUtils.listFiles(directory, new SuffixFileFilter(".pom"), TrueFileFilter.INSTANCE);
    }

    private Usage processPomFile(File pom, String group, String artifact) {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model = null;
        try {
            model = reader.read(new FileReader(pom));
            List<Dependency> dependencies = model.getDependencies();
            if (model.getDependencyManagement() != null) {
                dependencies.addAll(model.getDependencyManagement().getDependencies());
            }
            for (Dependency dependency : dependencies) {
                if (dependency.getGroupId().equals(group) && dependency.getArtifactId().equals(artifact)) {
                    // we have a match
                    String groupId = model.getGroupId() != null ? model.getGroupId() : model.getParent().getGroupId();
                    String version = model.getVersion() != null ? model.getVersion() : model.getParent().getVersion();
                    String versionUsed = dependency.getVersion() != null ? dependency.getVersion()
                            : "Inherited from Parent";
                    Usage usage = new Usage();
                    usage.setGroupId(groupId);
                    usage.setArtifactId(model.getArtifactId());
                    usage.setVersion(version);
                    if (model.getParent() != null) {
                        usage.setParentGroupId(model.getParent().getGroupId());
                        usage.setParentArtifactId(model.getParent().getArtifactId());
                    }
                    usage.setVersionUsed(versionUsed);
                    usage.setClassifier(dependency.getClassifier());
                    usage.setScope(dependency.getScope());
                    return usage;
                }
            }
        } catch (FileNotFoundException e) {
            LOGGER.error("unable to find pom file: " + pom.getAbsolutePath(), e);
        } catch (IOException e) {
            LOGGER.error("unable to read pom file: " + pom.getAbsolutePath(), e);
        } catch (XmlPullParserException e) {
            LOGGER.error("unable to parse pom file: " + pom.getAbsolutePath(), e);
        }
        return null;
    }

    private void outputCSV(List<Usage> results) {
        StringBuilder titles = new StringBuilder();
        titles.append("Group");
        titles.append(",");
        titles.append("Artifact");
        titles.append(",");
        titles.append("Version");
        titles.append(",");
        titles.append("Parent Group");
        titles.append(",");
        titles.append("Parent Artifact");
        titles.append(",");
        titles.append("Version Used");
        titles.append(",");
        titles.append("Classifier");
        titles.append(",");
        titles.append("Scope");
        System.out.println(titles.toString());
        for (Usage result : results) {
            StringBuilder builder = new StringBuilder();
            builder.append(result.getGroupId());
            builder.append(",");
            builder.append(result.getArtifactId());
            builder.append(",");
            builder.append(result.getVersion());
            builder.append(",");
            builder.append(result.getParentGroupId());
            builder.append(",");
            builder.append(result.getParentArtifactId());
            builder.append(",");
            builder.append(result.getVersionUsed());
            builder.append(",");
            builder.append(result.getClassifier());
            builder.append(",");
            builder.append(result.getScope());
            System.out.println(builder.toString());
        }
    }

}
