package com.castlemon.maven.processing;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.resolution.ArtifactDescriptorResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.castlemon.maven.control.Controller;
import com.castlemon.maven.domain.RunData;
import com.castlemon.maven.domain.Usage;

@Component
public class PomProcessor {

    @Autowired
    private AetherProcessor aetherProcessor;

    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);

    public void processPoms(RunData runData, Collection<File> pomFiles) {
        List<Usage> usages = new ArrayList<Usage>();
        MavenXpp3Reader reader = new MavenXpp3Reader();
        for (File pom : pomFiles) {
            try {
                Model model = reader.read(new FileReader(pom));
                String groupId = model.getGroupId() != null ? model.getGroupId() : model.getParent().getGroupId();
                String version = model.getVersion() != null ? model.getVersion() : model.getParent().getVersion();
                ArtifactDescriptorResult descriptorResult = aetherProcessor.getDirectDependencies(groupId,
                        model.getArtifactId(), version, runData);
                if (descriptorResult != null) {
                    usages.add(processDescriptor(descriptorResult, runData, pom.getAbsolutePath()));
                    runData.incrementPomsProcessed();
                } else {
                    runData.incrementPomsReadError();
                    runData.getPomsInError().add(pom.getAbsolutePath());
                }
            } catch (IOException e) {
                LOGGER.error("unable to read pom file: " + pom.getAbsolutePath());
                runData.incrementPomsReadError();
                runData.getPomsInError().add(pom.getAbsolutePath());
            } catch (XmlPullParserException e) {
                LOGGER.error("unable to parse pom file: " + pom.getAbsolutePath());
                runData.incrementPomsReadError();
                runData.getPomsInError().add(pom.getAbsolutePath());
            }
        }
        runData.setUsages(usages);
    }

    private Usage processDescriptor(ArtifactDescriptorResult descriptorResult, RunData runData, String pomPath) {
        Artifact artifactBeingProcessed = descriptorResult.getArtifact();
        List<Dependency> dependencies = descriptorResult.getDependencies();
        for (Dependency dependency : dependencies) {
            if (dependency.getArtifact().getGroupId().equals(runData.getGroup())
                    && dependency.getArtifact().getArtifactId().equals(runData.getArtifact())) {
                // we have a match
                Usage usage = new Usage();
                usage.setGroupId(artifactBeingProcessed.getGroupId());
                usage.setArtifactId(artifactBeingProcessed.getArtifactId());
                usage.setVersion(artifactBeingProcessed.getVersion());
                usage.setPath(pomPath);
                usage.setVersionUsed(dependency.getArtifact().getVersion());
                usage.setScope(dependency.getScope());
                return usage;
            }
        }
        return null;
    }

}
