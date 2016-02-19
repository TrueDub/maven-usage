package com.castlemon.maven.processing;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.impl.DefaultServiceLocator;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.LocalRepositoryManager;
import org.eclipse.aether.resolution.ArtifactDescriptorException;
import org.eclipse.aether.resolution.ArtifactDescriptorRequest;
import org.eclipse.aether.resolution.ArtifactDescriptorResult;
import org.eclipse.aether.resolution.VersionRangeRequest;
import org.eclipse.aether.resolution.VersionRangeResolutionException;
import org.eclipse.aether.resolution.VersionRangeResult;
import org.eclipse.aether.version.Version;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.LoggerFactory;

import com.castlemon.maven.domain.RunData;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;

@SuppressWarnings({ "rawtypes", "unchecked" })
@RunWith(PowerMockRunner.class)
@PrepareForTest({ AetherProcessor.class, MavenRepositorySystemUtils.class, DefaultServiceLocator.class,
        DefaultRepositorySystemSession.class, VersionRangeResult.class })
public class AetherProcessorTest {

    @Mock
    private Appender mockAppender;

    @Captor
    private ArgumentCaptor<LoggingEvent> captorLoggingEvent;

    @Rule
    public TemporaryFolder tempInputFolder = new TemporaryFolder();

    @Before
    public void setup() {
        final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.addAppender(mockAppender);
    }

    @After
    public void teardown() {
        final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.detachAppender(mockAppender);
    }

    @Test
    public void testGetDirectDependencies() throws IOException {
        RunData runData = new RunData();
        File repoDir = tempInputFolder.newFolder();
        runData.setRepo(repoDir.getAbsolutePath());
        AetherProcessor aetherProcessor = new AetherProcessor();
        ArtifactDescriptorResult result = aetherProcessor.getDirectDependencies("junit", "junit", "4.11", runData);
        Assert.assertEquals(0, result.getDependencies().size());
    }

    @Test
    public void testGetDirectDependenciesArtifactDescriptorException() throws IOException, ArtifactDescriptorException {
        RunData runData = new RunData();
        File repoDir = tempInputFolder.newFolder();
        runData.setRepo(repoDir.getAbsolutePath());
        RepositorySystem repositorySystem = mock(RepositorySystem.class);
        DefaultRepositorySystemSession session = PowerMockito.mock(DefaultRepositorySystemSession.class);
        when(repositorySystem.readArtifactDescriptor(any(RepositorySystemSession.class),
                any(ArtifactDescriptorRequest.class))).thenThrow(
                        new ArtifactDescriptorException(new ArtifactDescriptorResult(new ArtifactDescriptorRequest()),
                                "fred"));
        LocalRepositoryManager localRepositoryManager = Mockito.mock(LocalRepositoryManager.class);
        when(repositorySystem.newLocalRepositoryManager(any(RepositorySystemSession.class), any(LocalRepository.class)))
                .thenReturn(localRepositoryManager);
        DefaultServiceLocator defaultServiceLocator = PowerMockito.mock(DefaultServiceLocator.class);
        when(defaultServiceLocator.getService(RepositorySystem.class)).thenReturn(repositorySystem);
        PowerMockito.mockStatic(MavenRepositorySystemUtils.class);
        when(MavenRepositorySystemUtils.newServiceLocator()).thenReturn(defaultServiceLocator);
        when(MavenRepositorySystemUtils.newSession()).thenReturn(session);
        AetherProcessor aetherProcessor = new AetherProcessor();
        ArtifactDescriptorResult result = aetherProcessor.getDirectDependencies("junit", "junit", "4.11", runData);
        verify(mockAppender).doAppend(captorLoggingEvent.capture());
        PowerMockito.verifyStatic();
        Assert.assertNull(result);
        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        Assert.assertThat(loggingEvent.getLevel(), is(Level.ERROR));
        Assert.assertThat(loggingEvent.getFormattedMessage(), is("unable to retrieve descriptor for junit:junit:4.11"));
    }

    @Test
    public void testgetLatestVersion() throws IOException, VersionRangeResolutionException {
        RunData runData = new RunData();
        File repoDir = tempInputFolder.newFolder();
        runData.setRepo(repoDir.getAbsolutePath());
        RepositorySystem repositorySystem = mock(RepositorySystem.class);
        DefaultRepositorySystemSession session = PowerMockito.mock(DefaultRepositorySystemSession.class);
        VersionRangeResult rangeResult = PowerMockito.mock(VersionRangeResult.class);
        Version version = Mockito.mock(Version.class);
        when(version.toString()).thenReturn("1.2.3");
        when(rangeResult.getHighestVersion()).thenReturn(version);
        when(repositorySystem.resolveVersionRange(any(RepositorySystemSession.class), any(VersionRangeRequest.class)))
                .thenReturn(rangeResult);
        LocalRepositoryManager localRepositoryManager = Mockito.mock(LocalRepositoryManager.class);
        when(repositorySystem.newLocalRepositoryManager(any(RepositorySystemSession.class), any(LocalRepository.class)))
                .thenReturn(localRepositoryManager);
        DefaultServiceLocator defaultServiceLocator = PowerMockito.mock(DefaultServiceLocator.class);
        when(defaultServiceLocator.getService(RepositorySystem.class)).thenReturn(repositorySystem);
        PowerMockito.mockStatic(MavenRepositorySystemUtils.class);
        when(MavenRepositorySystemUtils.newServiceLocator()).thenReturn(defaultServiceLocator);
        when(MavenRepositorySystemUtils.newSession()).thenReturn(session);
        AetherProcessor aetherProcessor = new AetherProcessor();
        String result = aetherProcessor.getLatestVersion("junit", "junit", runData);
        Assert.assertEquals("1.2.3", result);
    }

    @Test
    public void testGetLatestVersionException() throws IOException, VersionRangeResolutionException {
        RunData runData = new RunData();
        File repoDir = tempInputFolder.newFolder();
        runData.setRepo(repoDir.getAbsolutePath());
        RepositorySystem repositorySystem = mock(RepositorySystem.class);
        DefaultRepositorySystemSession session = PowerMockito.mock(DefaultRepositorySystemSession.class);
        VersionRangeResult rangeResult = PowerMockito.mock(VersionRangeResult.class);
        Version version = Mockito.mock(Version.class);
        when(version.toString()).thenReturn("1.2.3");
        when(rangeResult.getHighestVersion()).thenReturn(version);
        VersionRangeResolutionException exp = Mockito.mock(VersionRangeResolutionException.class);
        when(exp.getMessage()).thenReturn("output message");
        when(repositorySystem.resolveVersionRange(any(RepositorySystemSession.class), any(VersionRangeRequest.class)))
                .thenThrow(exp);
        LocalRepositoryManager localRepositoryManager = Mockito.mock(LocalRepositoryManager.class);
        when(repositorySystem.newLocalRepositoryManager(any(RepositorySystemSession.class), any(LocalRepository.class)))
                .thenReturn(localRepositoryManager);
        DefaultServiceLocator defaultServiceLocator = PowerMockito.mock(DefaultServiceLocator.class);
        when(defaultServiceLocator.getService(RepositorySystem.class)).thenReturn(repositorySystem);
        PowerMockito.mockStatic(MavenRepositorySystemUtils.class);
        when(MavenRepositorySystemUtils.newServiceLocator()).thenReturn(defaultServiceLocator);
        when(MavenRepositorySystemUtils.newSession()).thenReturn(session);
        AetherProcessor aetherProcessor = new AetherProcessor();
        String result = aetherProcessor.getLatestVersion("junit", "junit", runData);
        verify(mockAppender).doAppend(captorLoggingEvent.capture());
        PowerMockito.verifyStatic();
        Assert.assertNull(result);
        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        Assert.assertThat(loggingEvent.getLevel(), is(Level.ERROR));
        Assert.assertThat(loggingEvent.getFormattedMessage(), is("unable to determine latest version for junit:junit"));
    }

}
