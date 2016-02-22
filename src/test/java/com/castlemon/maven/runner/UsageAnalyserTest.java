package com.castlemon.maven.runner;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.FileInputStream;
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
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;

import com.castlemon.maven.control.Controller;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;

@SuppressWarnings({ "rawtypes", "unchecked" })
@RunWith(PowerMockRunner.class)
@PrepareForTest({ UsageAnalyser.class, AnnotationConfigApplicationContext.class, FileInputStream.class })
public class UsageAnalyserTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Mock
    private Appender mockAppender;

    @Captor
    private ArgumentCaptor<LoggingEvent> captorLoggingEvent;

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
    public void testMain() throws Exception {
        String configFileLocation = "--config=" + getConfigFile();
        String[] args = { configFileLocation };

        MutablePropertySources propSources = new MutablePropertySources();
        ConfigurableEnvironment configurableEnvironment = Mockito.mock(ConfigurableEnvironment.class);
        Mockito.when(configurableEnvironment.getPropertySources()).thenReturn(propSources);
        AnnotationConfigApplicationContext context = Mockito.mock(AnnotationConfigApplicationContext.class);
        Mockito.when(context.getEnvironment()).thenReturn(configurableEnvironment);
        Controller controller = Mockito.mock(Controller.class);
        Mockito.when(context.getBean(Mockito.anyString())).thenReturn(controller);
        PowerMockito.whenNew(AnnotationConfigApplicationContext.class).withNoArguments().thenReturn(context);
        UsageAnalyser.main(args);
        verify(mockAppender).doAppend(captorLoggingEvent.capture());
        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        Assert.assertThat(loggingEvent.getLevel(), is(Level.INFO));
        Assert.assertThat(loggingEvent.getFormattedMessage(), is("Analysis run complete"));
    }

    @Test
    public void testMainIOException() throws Exception {
        String configFileLocation = "--config=" + getConfigFile();
        String[] args = { configFileLocation };
        MutablePropertySources propSources = new MutablePropertySources();
        ConfigurableEnvironment configurableEnvironment = Mockito.mock(ConfigurableEnvironment.class);
        Mockito.when(configurableEnvironment.getPropertySources()).thenReturn(propSources);
        AnnotationConfigApplicationContext context = Mockito.mock(AnnotationConfigApplicationContext.class);
        Mockito.when(context.getEnvironment()).thenReturn(configurableEnvironment);
        Controller controller = Mockito.mock(Controller.class);
        Mockito.when(context.getBean(Mockito.anyString())).thenReturn(controller);
        PowerMockito.whenNew(AnnotationConfigApplicationContext.class).withNoArguments().thenReturn(context);
        PowerMockito.whenNew(FileInputStream.class).withParameterTypes(String.class).withArguments(Mockito.anyString())
                .thenThrow(new IOException("fred"));
        UsageAnalyser.main(args);
        verify(mockAppender, Mockito.times(2)).doAppend(captorLoggingEvent.capture());
        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        Assert.assertThat(loggingEvent.getLevel(), is(Level.INFO));
        Assert.assertThat(loggingEvent.getFormattedMessage(), is("Analysis run complete"));
    }

    @Test
    public void testMainWrongArgs() throws Exception {
        String[] args = {};
        UsageAnalyser.main(args);
        verify(mockAppender, Mockito.times(2)).doAppend(captorLoggingEvent.capture());
        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        Assert.assertThat(loggingEvent.getLevel(), is(Level.ERROR));
        Assert.assertThat(loggingEvent.getFormattedMessage(),
                is("Usage: java -jar maven-usage.jar --config=<path to config file>"));
    }

    private String getConfigFile() {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("test-config.properties").getFile());
        return file.getAbsolutePath();
    }

}
