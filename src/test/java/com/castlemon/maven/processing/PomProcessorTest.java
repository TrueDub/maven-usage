package com.castlemon.maven.processing;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.resolution.ArtifactDescriptorResult;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
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
@PrepareForTest({ PomProcessor.class, MavenXpp3Reader.class, ArtifactDescriptorResult.class })
public class PomProcessorTest {

    @Mock
    private Appender mockAppender;

    @Mock
    private AetherProcessor aetherProcessor;

    @InjectMocks
    private PomProcessor pomProcessor;

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
    public void testProcessPoms() throws Exception {
        Model model1 = new Model();
        model1.setGroupId("group1");
        model1.setArtifactId("artifact1");
        model1.setVersion("version1");
        Model model2 = new Model();
        Parent parent = new Parent();
        parent.setGroupId("groupp2");
        parent.setVersion("groupp2");
        model2.setParent(parent);
        Model model3 = new Model();
        model3.setGroupId("group3");
        model3.setArtifactId("artifact3");
        model3.setVersion("version3");
        MavenXpp3Reader reader = Mockito.mock(MavenXpp3Reader.class);
        Mockito.when(reader.read(Mockito.any(FileReader.class))).thenReturn(model1).thenReturn(model2);
        PowerMockito.whenNew(MavenXpp3Reader.class).withNoArguments().thenReturn(reader);

        ArtifactDescriptorResult descriptorResult1 = generateArtifactDescriptorResult();
        Mockito.when(aetherProcessor.getDirectDependencies(Mockito.anyString(), Mockito.anyString(),
                Mockito.anyString(), Mockito.any(RunData.class))).thenReturn(descriptorResult1).thenReturn(null)
                .thenReturn(generateEmptyArtifactDescriptorResult());
        RunData runData = new RunData();
        runData.setGroup("search1");
        runData.setArtifact("searchA1");
        File file1 = tempInputFolder.newFile("a");
        File file2 = tempInputFolder.newFile("b");
        File file3 = tempInputFolder.newFile("c");
        File[] fileArray = { file1, file2, file3 };
        List<File> files = Arrays.asList(fileArray);
        pomProcessor.processPoms(runData, files);
        Assert.assertEquals(1, runData.getPomsReadError());
        Assert.assertEquals(2, runData.getPomsProcessed());
        Assert.assertEquals(2, runData.getUsages().size());
    }

    @Test
    public void testProcessPomsXmlPullParserException() throws Exception {
        MavenXpp3Reader reader = Mockito.mock(MavenXpp3Reader.class);
        Mockito.when(reader.read(Mockito.any(FileReader.class))).thenThrow(new XmlPullParserException("fred"));
        PowerMockito.whenNew(MavenXpp3Reader.class).withNoArguments().thenReturn(reader);
        RunData runData = new RunData();
        runData.setGroup("search1");
        runData.setArtifact("searchA1");
        File file1 = tempInputFolder.newFile("a");
        File[] fileArray = { file1 };
        List<File> files = Arrays.asList(fileArray);
        pomProcessor.processPoms(runData, files);
        verify(mockAppender).doAppend(captorLoggingEvent.capture());
        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        Assert.assertThat(loggingEvent.getLevel(), is(Level.ERROR));
        Assert.assertThat(loggingEvent.getFormattedMessage(),
                is("unable to parse pom file: " + file1.getAbsolutePath()));
        Assert.assertEquals(1, runData.getPomsReadError());
        Assert.assertEquals(0, runData.getPomsProcessed());
        Assert.assertEquals(0, runData.getUsages().size());
    }

    @Test
    public void testProcessPomsIOException() throws Exception {
        MavenXpp3Reader reader = Mockito.mock(MavenXpp3Reader.class);
        Mockito.when(reader.read(Mockito.any(FileReader.class))).thenThrow(new IOException());
        PowerMockito.whenNew(MavenXpp3Reader.class).withNoArguments().thenReturn(reader);
        RunData runData = new RunData();
        runData.setGroup("search1");
        runData.setArtifact("searchA1");
        File file1 = tempInputFolder.newFile("a");
        File[] fileArray = { file1 };
        List<File> files = Arrays.asList(fileArray);
        pomProcessor.processPoms(runData, files);
        verify(mockAppender).doAppend(captorLoggingEvent.capture());
        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        Assert.assertThat(loggingEvent.getLevel(), is(Level.ERROR));
        Assert.assertThat(loggingEvent.getFormattedMessage(),
                is("unable to read pom file: " + file1.getAbsolutePath()));
        Assert.assertEquals(1, runData.getPomsReadError());
        Assert.assertEquals(0, runData.getPomsProcessed());
        Assert.assertEquals(0, runData.getUsages().size());
    }

    private ArtifactDescriptorResult generateArtifactDescriptorResult() {
        ArtifactDescriptorResult descriptorResult1 = PowerMockito.mock(ArtifactDescriptorResult.class);
        Artifact artifact = new DefaultArtifact("groupX", "artifactX", "jar", "versionX");
        Mockito.when(descriptorResult1.getArtifact()).thenReturn(artifact);

        Artifact artifactDep1 = new DefaultArtifact("search1", "searchA1", "jar", "version1");
        Dependency dependency1 = new Dependency(artifactDep1, "fred");

        Artifact artifactDep2 = new DefaultArtifact("group999", "artifact999", "jar", "version999");
        Dependency dependency2 = new Dependency(artifactDep2, "bill");

        Artifact artifactDep3 = new DefaultArtifact("search1", "artifactA99", "jar", "version999");
        Dependency dependency3 = new Dependency(artifactDep3, "bill");

        Artifact artifactDep4 = new DefaultArtifact("search1999", "searchA1", "jar", "version1");
        Dependency dependency4 = new Dependency(artifactDep4, "fred");

        Dependency[] dependenciesArray = { dependency2, dependency3, dependency4, dependency1 };
        List<Dependency> dependencies = Arrays.asList(dependenciesArray);
        Mockito.when(descriptorResult1.getDependencies()).thenReturn(dependencies);
        return descriptorResult1;
    }

    private ArtifactDescriptorResult generateEmptyArtifactDescriptorResult() {
        ArtifactDescriptorResult descriptorResult1 = PowerMockito.mock(ArtifactDescriptorResult.class);
        Artifact artifact = new DefaultArtifact("groupXe", "artifactXe", "jar", "versionX");
        Mockito.when(descriptorResult1.getArtifact()).thenReturn(artifact);
        Mockito.when(descriptorResult1.getDependencies()).thenReturn(new ArrayList<Dependency>());
        return descriptorResult1;
    }

}
