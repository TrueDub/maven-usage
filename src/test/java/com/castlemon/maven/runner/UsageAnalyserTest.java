package com.castlemon.maven.runner;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.FileInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

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
    public void testPrivateConstructor() throws NoSuchMethodException, SecurityException, InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Constructor<UsageAnalyser> c = UsageAnalyser.class.getDeclaredConstructor();
        c.setAccessible(true);
        Assert.assertTrue(c.newInstance() instanceof UsageAnalyser);
    }

    @Test
    public void testMainDefaults() throws Exception {
        String group = "--group=junit";
        String artifact = "--artifact=junit";
        String outputDir = "--outputDir=" + tempFolder.newFolder();
        String[] args = { group, artifact, outputDir };
        MutablePropertySources propSources = new MutablePropertySources();
        ConfigurableEnvironment configurableEnvironment = Mockito.mock(ConfigurableEnvironment.class);
        Mockito.when(configurableEnvironment.getPropertySources()).thenReturn(propSources);
        AnnotationConfigApplicationContext context = Mockito.mock(AnnotationConfigApplicationContext.class);
        Mockito.when(context.getEnvironment()).thenReturn(configurableEnvironment);
        Controller controller = Mockito.mock(Controller.class);
        Mockito.when(context.getBean(Mockito.anyString())).thenReturn(controller);
        PowerMockito.whenNew(AnnotationConfigApplicationContext.class).withNoArguments().thenReturn(context);
        UsageAnalyser.main(args);
        verify(mockAppender, times(4)).doAppend(captorLoggingEvent.capture());
        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        Assert.assertThat(loggingEvent.getLevel(), is(Level.INFO));
        Assert.assertThat(loggingEvent.getFormattedMessage(), is("Analysis run complete"));
    }

    @Test
    public void testMainNoDefaults() throws Exception {
        String group = "--group=junit";
        String artifact = "--artifact=junit";
        String outputDir = "--outputDir=" + tempFolder.newFolder();
        String searchDir = "--searchDir=" + tempFolder.newFolder();
        String outputFormats = "--outputFormats=CSV";
        String repo = "--repo=" + tempFolder.newFolder();
        String[] args = { group, artifact, outputDir, searchDir, outputFormats, repo };
        MutablePropertySources propSources = new MutablePropertySources();
        ConfigurableEnvironment configurableEnvironment = Mockito.mock(ConfigurableEnvironment.class);
        Mockito.when(configurableEnvironment.getPropertySources()).thenReturn(propSources);
        AnnotationConfigApplicationContext context = Mockito.mock(AnnotationConfigApplicationContext.class);
        Mockito.when(context.getEnvironment()).thenReturn(configurableEnvironment);
        Controller controller = Mockito.mock(Controller.class);
        Mockito.when(context.getBean(Mockito.anyString())).thenReturn(controller);
        PowerMockito.whenNew(AnnotationConfigApplicationContext.class).withNoArguments().thenReturn(context);
        UsageAnalyser.main(args);
        verify(mockAppender, times(1)).doAppend(captorLoggingEvent.capture());
        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        Assert.assertThat(loggingEvent.getLevel(), is(Level.INFO));
        Assert.assertThat(loggingEvent.getFormattedMessage(), is("Analysis run complete"));
    }

    @Test
    public void testMainOptions() throws Exception {
        String options = "--options";
        String[] args = { options };
        MutablePropertySources propSources = new MutablePropertySources();
        ConfigurableEnvironment configurableEnvironment = Mockito.mock(ConfigurableEnvironment.class);
        Mockito.when(configurableEnvironment.getPropertySources()).thenReturn(propSources);
        AnnotationConfigApplicationContext context = Mockito.mock(AnnotationConfigApplicationContext.class);
        Mockito.when(context.getEnvironment()).thenReturn(configurableEnvironment);
        Controller controller = Mockito.mock(Controller.class);
        Mockito.when(context.getBean(Mockito.anyString())).thenReturn(controller);
        PowerMockito.whenNew(AnnotationConfigApplicationContext.class).withNoArguments().thenReturn(context);
        UsageAnalyser.main(args);
        verify(mockAppender, times(15)).doAppend(captorLoggingEvent.capture());
        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        Assert.assertThat(loggingEvent.getLevel(), is(Level.INFO));
        Assert.assertThat(loggingEvent.getFormattedMessage(), is(" "));
    }

    @Test
    public void testMainHelp() throws Exception {
        String options = "--help";
        String[] args = { options };
        MutablePropertySources propSources = new MutablePropertySources();
        ConfigurableEnvironment configurableEnvironment = Mockito.mock(ConfigurableEnvironment.class);
        Mockito.when(configurableEnvironment.getPropertySources()).thenReturn(propSources);
        AnnotationConfigApplicationContext context = Mockito.mock(AnnotationConfigApplicationContext.class);
        Mockito.when(context.getEnvironment()).thenReturn(configurableEnvironment);
        Controller controller = Mockito.mock(Controller.class);
        Mockito.when(context.getBean(Mockito.anyString())).thenReturn(controller);
        PowerMockito.whenNew(AnnotationConfigApplicationContext.class).withNoArguments().thenReturn(context);
        UsageAnalyser.main(args);
        verify(mockAppender, times(15)).doAppend(captorLoggingEvent.capture());
        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        Assert.assertThat(loggingEvent.getLevel(), is(Level.INFO));
        Assert.assertThat(loggingEvent.getFormattedMessage(), is(" "));
    }

    @Test
    public void testMainNoGroup() throws Exception {
        String[] args = {};
        MutablePropertySources propSources = new MutablePropertySources();
        ConfigurableEnvironment configurableEnvironment = Mockito.mock(ConfigurableEnvironment.class);
        Mockito.when(configurableEnvironment.getPropertySources()).thenReturn(propSources);
        AnnotationConfigApplicationContext context = Mockito.mock(AnnotationConfigApplicationContext.class);
        Mockito.when(context.getEnvironment()).thenReturn(configurableEnvironment);
        Controller controller = Mockito.mock(Controller.class);
        Mockito.when(context.getBean(Mockito.anyString())).thenReturn(controller);
        PowerMockito.whenNew(AnnotationConfigApplicationContext.class).withNoArguments().thenReturn(context);
        UsageAnalyser.main(args);
        verify(mockAppender, times(17)).doAppend(captorLoggingEvent.capture());
        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        Assert.assertThat(loggingEvent.getLevel(), is(Level.INFO));
        Assert.assertThat(loggingEvent.getFormattedMessage(), is("Analysis run halted with errors"));
    }

    @Test
    public void testMainNoArtifact() throws Exception {
        String group = "--group=junit";
        String[] args = { group };
        MutablePropertySources propSources = new MutablePropertySources();
        ConfigurableEnvironment configurableEnvironment = Mockito.mock(ConfigurableEnvironment.class);
        Mockito.when(configurableEnvironment.getPropertySources()).thenReturn(propSources);
        AnnotationConfigApplicationContext context = Mockito.mock(AnnotationConfigApplicationContext.class);
        Mockito.when(context.getEnvironment()).thenReturn(configurableEnvironment);
        Controller controller = Mockito.mock(Controller.class);
        Mockito.when(context.getBean(Mockito.anyString())).thenReturn(controller);
        PowerMockito.whenNew(AnnotationConfigApplicationContext.class).withNoArguments().thenReturn(context);
        UsageAnalyser.main(args);
        verify(mockAppender, times(17)).doAppend(captorLoggingEvent.capture());
        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        Assert.assertThat(loggingEvent.getLevel(), is(Level.INFO));
        Assert.assertThat(loggingEvent.getFormattedMessage(), is("Analysis run halted with errors"));
    }

    @Test
    public void testMainNoOutputDir() throws Exception {
        String group = "--group=junit";
        String artifact = "--artifact=junit";
        String[] args = { group, artifact };
        MutablePropertySources propSources = new MutablePropertySources();
        ConfigurableEnvironment configurableEnvironment = Mockito.mock(ConfigurableEnvironment.class);
        Mockito.when(configurableEnvironment.getPropertySources()).thenReturn(propSources);
        AnnotationConfigApplicationContext context = Mockito.mock(AnnotationConfigApplicationContext.class);
        Mockito.when(context.getEnvironment()).thenReturn(configurableEnvironment);
        Controller controller = Mockito.mock(Controller.class);
        Mockito.when(context.getBean(Mockito.anyString())).thenReturn(controller);
        PowerMockito.whenNew(AnnotationConfigApplicationContext.class).withNoArguments().thenReturn(context);
        UsageAnalyser.main(args);
        verify(mockAppender, times(17)).doAppend(captorLoggingEvent.capture());
        final LoggingEvent loggingEvent = captorLoggingEvent.getValue();
        Assert.assertThat(loggingEvent.getLevel(), is(Level.INFO));
        Assert.assertThat(loggingEvent.getFormattedMessage(), is("Analysis run halted with errors"));
    }

}
