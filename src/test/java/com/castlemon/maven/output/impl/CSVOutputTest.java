package com.castlemon.maven.output.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.verify;

import java.io.File;

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
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.LoggerFactory;

import com.castlemon.maven.domain.RunData;
import com.castlemon.maven.domain.Usage;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;

@SuppressWarnings({ "unchecked", "rawtypes" })
@RunWith(MockitoJUnitRunner.class)
public class CSVOutputTest {

    @Mock
    private Appender mockAppender;

    // Captor is genericised with ch.qos.logback.classic.spi.LoggingEvent
    @Captor
    private ArgumentCaptor<LoggingEvent> captorLoggingEvent;

    @Rule
    public TemporaryFolder tempInputFolder = new TemporaryFolder();

    @Before
    public void setup() {
        final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.addAppender(mockAppender);
    }

    // Always have this teardown otherwise we can stuff up our expectations.
    @After
    public void teardown() {
        final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.detachAppender(mockAppender);
    }

    @Test
    public void testWriteCSVFile() throws Exception {
        Usage usage = new Usage();
        RunData runData = new RunData();
        runData.getUsages().add(usage);
        File searchDir = tempInputFolder.newFolder();
        runData.setSearchDirectory(searchDir.getAbsolutePath());
        File outputDir = tempInputFolder.newFolder();
        runData.setOutputDirectory(outputDir.getAbsolutePath());
        CSVOutput csvOutput = new CSVOutput();
        csvOutput.writeData(runData);
        File report = new File(runData.getOutputDirectory() + File.separator + "usage.csv");
        Assert.assertTrue(report.exists());
    }

    @Test
    public void testWriteCSVFileFileNotFoundException() throws Exception {
        RunData runData = new RunData();
        CSVOutput csvOutput = new CSVOutput();
        csvOutput.writeData(runData);
        verify(mockAppender).doAppend(captorLoggingEvent.capture());
        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        // Check log level is correct
        Assert.assertThat(loggingEvent.getLevel(), is(Level.ERROR));
        // Check the message being logged is correct
        Assert.assertThat(loggingEvent.getFormattedMessage(), is("could not write CSV to null"));
    }

}
