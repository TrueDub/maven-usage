package com.castlemon.maven.processing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.castlemon.maven.control.Controller;
import com.castlemon.maven.domain.Usage;

@Component
public class PomProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);

    public Map<String, Model> preProcessPoms(Collection<File> pomFiles) {
        Map<String, Model> models = new HashMap<String, Model>();
        MavenXpp3Reader reader = new MavenXpp3Reader();
        for (File pom : pomFiles) {
            try {
                Model model = reader.read(new FileReader(pom));
                models.put(model.getGroupId() + ":" + model.getArtifactId() + ":" + model.getVersion(), model);
            } catch (FileNotFoundException e) {
                LOGGER.error("unable to find pom file: " + pom.getAbsolutePath());
            } catch (IOException e) {
                LOGGER.error("unable to read pom file: " + pom.getAbsolutePath());
            } catch (XmlPullParserException e) {
                LOGGER.error("unable to parse pom file: " + pom.getAbsolutePath());
            }
        }
        return models;
    }

    public Usage processPomFile(Model model, String group, String artifact) {
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
                usage.setVersionInherited(determineVersionUsed(model, dependency, usage));
                usage.setScope(dependency.getScope());
                return usage;
            }
        }
        return null;
    }

    public void processParentVersions(Collection<Usage> usages, Map<String, Model> models, String group,
            String artifact) {
        for (Usage usage : usages) {
            if (usage.isVersionInherited()) {
                processParentModel(models, group, artifact, usage);
            }
        }
    }

    private void processParentModel(Map<String, Model> models, String group, String artifact, Usage usage) {
        if (!models.containsKey(usage.getParentIdentifier())) {
            System.out.println("Processing parent " + usage.getParentIdentifier() + " for " + usage.getIdentifier()
                    + " - not found");
            return;
        }
        Model parentModel = models.get(usage.getParentIdentifier());
        List<Dependency> dependencies = new ArrayList<Dependency>();
        if (parentModel.getDependencies() != null) {
            dependencies.addAll(parentModel.getDependencies());
        }
        if (parentModel.getDependencyManagement() != null) {
            dependencies.addAll(parentModel.getDependencyManagement().getDependencies());
        }
        for (Dependency dependency : dependencies) {
            if (dependency.getGroupId().equals(group) && dependency.getArtifactId().equals(artifact)) {
                boolean furtherCheckNeeded = determineVersionUsed(parentModel, dependency, usage);
                if (furtherCheckNeeded) {
                    System.out.println("3 levels of inheritance - " + usage.getParentGroupId()
                            + usage.getParentArtifactId() + usage.getParentVersion());
                }
                break;
            }
        }
    }

    private boolean determineVersionUsed(Model model, Dependency dependency, Usage usage) {
        if (dependency.getVersion() == null) {
            // version inherited from parent
            usage.setVersionUsed("inherited");
            return true;
        } else {
            return getVersionNumber(model, dependency, usage);
        }
    }

    private boolean getVersionNumber(Model model, Dependency dependency, Usage usage) {
        // determine if property used and if so resolve
        if (dependency.getVersion().charAt(0) == '$') {
            String property = StringUtils.substringBetween(dependency.getVersion(), "{", "}");
            if (model.getProperties().containsKey(property)) {
                usage.setVersionUsed(model.getProperties().getProperty(property));
                return false;
            } else {
                usage.setVersionUsed("inherited");
                return true;
            }
        } else {
            usage.setVersionUsed(dependency.getVersion());
            return false;
        }
    }

}
