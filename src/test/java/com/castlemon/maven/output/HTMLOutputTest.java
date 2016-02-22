package com.castlemon.maven.output;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;

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
import com.castlemon.maven.domain.Usage;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import freemarker.template.Configuration;

@SuppressWarnings({ "rawtypes", "unchecked" })
@RunWith(PowerMockRunner.class)
@PrepareForTest({ HTMLOutput.class, Configuration.class })
public class HTMLOutputTest {

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
    public void testWriteHTMLOutput() throws Exception {
        Usage usage = new Usage();
        RunData runData = new RunData();
        runData.getUsages().add(usage);
        File searchDir = tempInputFolder.newFolder();
        runData.setSearchDirectory(searchDir.getAbsolutePath());
        File outputDir = tempInputFolder.newFolder();
        runData.setOutputDirectory(outputDir.getAbsolutePath());
        HTMLOutput htmlOutput = new HTMLOutput();
        htmlOutput.writeHTMLOutput(runData);
        File report = new File(runData.getOutputDirectory() + File.separator + "usagereport.html");
        Assert.assertTrue(report.exists());
    }

    @Test
    public void testWriteHTMLOutputDoesntExist() throws Exception {
        RunData runData = new RunData();
        runData.setOutputDirectory("fred");
        HTMLOutput htmlOutput = new HTMLOutput();
        htmlOutput.writeHTMLOutput(runData);
        verify(mockAppender).doAppend(captorLoggingEvent.capture());
        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        Assert.assertThat(loggingEvent.getLevel(), is(Level.ERROR));
        Assert.assertThat(loggingEvent.getFormattedMessage(), is("Output directory does not exist: fred"));
    }

    @Test
    public void testWriteHTMLOutputIOException() throws Exception {
        RunData runData = new RunData();
        File searchDir = tempInputFolder.newFolder();
        runData.setSearchDirectory(searchDir.getAbsolutePath());
        File outputDir = tempInputFolder.newFolder();
        runData.setOutputDirectory(outputDir.getAbsolutePath());
        Configuration configuration = Mockito.mock(Configuration.class);
        Mockito.when(configuration.getTemplate(Mockito.anyString())).thenThrow(new IOException());
        PowerMockito.whenNew(Configuration.class).withAnyArguments().thenReturn(configuration);
        HTMLOutput htmlOutput = new HTMLOutput();
        htmlOutput.writeHTMLOutput(runData);
        verify(mockAppender).doAppend(captorLoggingEvent.capture());
        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        Assert.assertThat(loggingEvent.getLevel(), is(Level.ERROR));
        Assert.assertThat(loggingEvent.getFormattedMessage(), is("I/O Error when writing HTML file"));
    }

}
