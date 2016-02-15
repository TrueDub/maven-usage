package com.castlemon.maven.processing;

import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.impl.DefaultServiceLocator;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.resolution.ArtifactDescriptorException;
import org.eclipse.aether.resolution.ArtifactDescriptorRequest;
import org.eclipse.aether.resolution.ArtifactDescriptorResult;
import org.eclipse.aether.resolution.VersionRangeRequest;
import org.eclipse.aether.resolution.VersionRangeResolutionException;
import org.eclipse.aether.resolution.VersionRangeResult;
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory;
import org.eclipse.aether.spi.connector.transport.TransporterFactory;
import org.eclipse.aether.transport.file.FileTransporterFactory;
import org.eclipse.aether.transport.http.HttpTransporterFactory;
import org.eclipse.aether.version.Version;
import org.springframework.stereotype.Component;

@Component
public class AetherProcessor {

    public String getLatestVersion(String groupId, String artifactId) {
        RepositorySystem system = newRepositorySystem();
        RepositorySystemSession session = newSession(system);
        Artifact artifact = new DefaultArtifact(groupId + ":" + artifactId + ":[0,)");
        VersionRangeRequest rangeRequest = new VersionRangeRequest();
        rangeRequest.setArtifact(artifact);
        // rangeRequest.setRepositories(newRepositories(system, session));
        VersionRangeResult rangeResult = null;
        try {
            rangeResult = system.resolveVersionRange(session, rangeRequest);
        } catch (VersionRangeResolutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Version newestVersion = rangeResult.getHighestVersion();
        return newestVersion.toString();
    }

    public void getDirectDependencies(String groupId, String artifactId, String version) {
        RepositorySystem system = newRepositorySystem();
        RepositorySystemSession session = newSession(system);
        Artifact artifact = new DefaultArtifact(groupId + ":" + artifactId + ":" + version);
        ArtifactDescriptorRequest descriptorRequest = new ArtifactDescriptorRequest();
        descriptorRequest.setArtifact(artifact);
        ArtifactDescriptorResult descriptorResult = null;
        try {
            descriptorResult = system.readArtifactDescriptor(session, descriptorRequest);
        } catch (ArtifactDescriptorException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        for (Dependency dependency : descriptorResult.getDependencies()) {
            System.out.println(dependency);
        }
    }

    private RepositorySystemSession newSession(RepositorySystem system) {
        DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();

        LocalRepository localRepo = new LocalRepository("C:\\Users\\gallagherji\\.m2\\repository");
        session.setLocalRepositoryManager(system.newLocalRepositoryManager(session, localRepo));

        return session;
    }

    private RepositorySystem newRepositorySystem() {
        DefaultServiceLocator locator = MavenRepositorySystemUtils.newServiceLocator();
        locator.addService(RepositoryConnectorFactory.class, BasicRepositoryConnectorFactory.class);
        locator.addService(TransporterFactory.class, FileTransporterFactory.class);
        locator.addService(TransporterFactory.class, HttpTransporterFactory.class);
        return locator.getService(RepositorySystem.class);
    }

}
