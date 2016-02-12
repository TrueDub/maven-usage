package com.castlemon.maven.processing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.apache.maven.shared.invoker.SystemOutHandler;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.castlemon.maven.control.Controller;
import com.castlemon.maven.domain.RunData;
import com.castlemon.maven.domain.Usage;

@Component
public class PomProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);

    public void processPoms(RunData runData, Collection<File> pomFiles) {
        List<Usage> usages = new ArrayList<Usage>();
        MavenXpp3Reader reader = new MavenXpp3Reader();
        File tempDir = makeDir("temp", runData.getOutputDirectory());
        for (File pom : pomFiles) {
            try {
                Model model = reader.read(new FileReader(pom));
                if (model.getPackaging().equals("pom")) {
                    // ignore parent and aggregator poms
                    runData.incrementPomsNotProcessed();
                    runData.getPomsNotToBeProcessed().add(pom.getAbsolutePath());
                    continue;
                }
                // copy pom to temp dir
                File tempPom = copyFile(pom, tempDir);
                // generate maven effective pom
                File effectivePom = generateMavenEffectivePom(tempPom, tempDir, runData);
                // read effective pom as model & store
                if (effectivePom != null) {
                    Model effectiveModel = reader.read(new FileReader(effectivePom));
                    usages.add(processPomFile(effectiveModel, runData.getGroup(), runData.getArtifact()));
                    effectivePom.delete();
                    effectivePom = null;
                }
                // clean up temp dir
                tempPom.delete();
                tempPom = null;
            } catch (FileNotFoundException e) {
                LOGGER.error("unable to find pom file: " + pom.getAbsolutePath());
            } catch (IOException e) {
                LOGGER.error("unable to read pom file: " + pom.getAbsolutePath());
            } catch (XmlPullParserException e) {
                LOGGER.error("unable to parse pom file: " + pom.getAbsolutePath());
            }
        }
        removeDir("temp", runData.getOutputDirectory());
        runData.setUsages(usages);
    }

    private Usage processPomFile(Model model, String group, String artifact) {
        List<Dependency> dependencies = model.getDependencies();
        if (model.getDependencyManagement() != null) {
            dependencies.addAll(model.getDependencyManagement().getDependencies());
        }
        for (Dependency dependency : dependencies) {
            if (dependency.getGroupId().equals(group) && dependency.getArtifactId().equals(artifact)) {
                // we have a match
                String groupId = model.getGroupId() != null ? model.getGroupId() : model.getParent().getGroupId();
                String version = model.getVersion() != null ? model.getVersion() : model.getParent().getVersion();
                Usage usage = new Usage();
                usage.setGroupId(groupId);
                usage.setArtifactId(model.getArtifactId());
                usage.setVersion(version);
                if (model.getPackaging() != null) {
                    usage.setPackaging(model.getPackaging());
                } else {
                    usage.setPackaging("jar");
                }
                if (model.getParent() != null) {
                    usage.setParentGroupId(model.getParent().getGroupId());
                    usage.setParentArtifactId(model.getParent().getArtifactId());
                    usage.setParentVersion(model.getParent().getVersion());
                }
                usage.setVersionUsed(dependency.getVersion());
                usage.setScope(dependency.getScope());
                return usage;
            }
        }
        return null;
    }

    private File copyFile(File fileName, File tempDir) throws FileNotFoundException, IOException {
        InputStream resourceArchiveInputStream = null;
        FileOutputStream outStream = null;
        File outFile = new File(tempDir.getAbsolutePath() + "/" + fileName.getName());
        try {
            resourceArchiveInputStream = new FileInputStream(fileName);
            outStream = new FileOutputStream(outFile);
            IOUtils.copy(resourceArchiveInputStream, outStream);
            return outFile;
        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            throw e;
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

    private void removeDir(String dirName, String outputDir) {
        try {
            FileUtils.deleteDirectory(new File(outputDir + File.separator + dirName));
        } catch (IOException e) {
            LOGGER.error("Error deleting directory " + outputDir + File.separator + dirName, e);
        }
    }

    private File generateMavenEffectivePom(File pom, File tempDir, RunData runData) {
        InvocationRequest request = new DefaultInvocationRequest();
        request.setErrorHandler(new SystemOutHandler());
        request.setOutputHandler(null);
        request.setPomFile(pom);
        String effectiveName = pom.getName() + "-effective";
        request.setGoals(Collections.singletonList("help:effective-pom -Doutput=" + effectiveName));
        Invoker invoker = new DefaultInvoker();
        invoker.setMavenHome(new File("C:\\apache-maven-3.3.3"));
        try {
            InvocationResult result = invoker.execute(request);
            if (result.getExitCode() == 0) {
                File returnFile = new File(tempDir.getAbsolutePath() + "/" + effectiveName);
                runData.incrementPomsProcessed();
                return returnFile;
            } else {
                LOGGER.error("Unable to generate effective pom for " + pom.getAbsolutePath());
                runData.incrementPomsReadError();
                runData.getPomsInError().add(pom.getAbsolutePath());
                return null;
            }
        } catch (MavenInvocationException e) {
            LOGGER.error("Error generating effective pom for " + pom.getAbsolutePath(), e);
        }
        return null;
    }

}
