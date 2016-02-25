package com.castlemon.maven.processing;

import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory;
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
import org.eclipse.aether.version.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.castlemon.maven.domain.RunData;

@Component
public class AetherProcessor {

  private static final Logger LOGGER = LoggerFactory.getLogger(AetherProcessor.class);

  public String getLatestVersion(String groupId, String artifactId, RunData runData) {
    RepositorySystem system = newRepositorySystem();
    RepositorySystemSession session = newSession(system, runData);
    Artifact artifact = new DefaultArtifact(groupId + ":" + artifactId + ":[0,)");
    VersionRangeRequest rangeRequest = new VersionRangeRequest();
    rangeRequest.setArtifact(artifact);
    VersionRangeResult rangeResult = null;
    try {
      rangeResult = system.resolveVersionRange(session, rangeRequest);
    } catch (VersionRangeResolutionException e) {
      LOGGER.error("unable to determine latest version for " + groupId + ":" + artifactId);
      return null;
    }
    Version newestVersion = rangeResult.getHighestVersion();
    return newestVersion.toString();
  }

  public ArtifactDescriptorResult getDirectDependencies(String groupId, String artifactId, String version,
      RunData runData) {
    RepositorySystem system = newRepositorySystem();
    RepositorySystemSession session = newSession(system, runData);
    Artifact artifact = new DefaultArtifact(groupId + ":" + artifactId + ":" + version);
    ArtifactDescriptorRequest descriptorRequest = new ArtifactDescriptorRequest();
    descriptorRequest.setArtifact(artifact);
    ArtifactDescriptorResult descriptorResult = null;
    try {
      descriptorResult = system.readArtifactDescriptor(session, descriptorRequest);
      return descriptorResult;
    } catch (ArtifactDescriptorException e) {
      LOGGER.error("unable to retrieve descriptor for " + groupId + ":" + artifactId + ":" + version);
    }
    return null;
  }

  private RepositorySystemSession newSession(RepositorySystem system, RunData runData) {
    DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();
    LocalRepository localRepo = new LocalRepository(runData.getRepo());
    session.setLocalRepositoryManager(system.newLocalRepositoryManager(session, localRepo));
    return session;
  }

  private RepositorySystem newRepositorySystem() {
    DefaultServiceLocator locator = MavenRepositorySystemUtils.newServiceLocator();
    locator.addService(RepositoryConnectorFactory.class, BasicRepositoryConnectorFactory.class);
    locator.addService(TransporterFactory.class, FileTransporterFactory.class);
    return locator.getService(RepositorySystem.class);
  }

}
