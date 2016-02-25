package com.castlemon.maven.control;

import java.io.File;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.castlemon.maven.domain.RunData;
import com.castlemon.maven.output.OutputFactory;
import com.castlemon.maven.processing.PomProcessor;
import com.castlemon.maven.processing.StatsGenerator;

@RunWith(MockitoJUnitRunner.class)
public class ControllerTest {

    @Mock
    private PomProcessor pomProcessor;

    @Mock
    private StatsGenerator statsGenerator;

    @Mock
    private OutputFactory outputFactory;

    @Rule
    public TemporaryFolder tempInputFolder = new TemporaryFolder();

    @InjectMocks
    private Controller controller = new Controller();

    @Test
    public void testExecuteAnalysis() throws IOException {
        RunData runData = new RunData();
        runData.getOutputFormats().add("csv");
        runData.getOutputFormats().add("html");
        File searchDir = tempInputFolder.newFolder();
        runData.setSearchDirectory(searchDir.getAbsolutePath());
        controller.executeAnalysis(runData);
        Mockito.verify(outputFactory, Mockito.times(2)).getOutput(Mockito.anyString());
    }

    @Test
    public void testExecuteAnalysisCSVOnly() throws IOException {
        RunData runData = new RunData();
        runData.getOutputFormats().add("csv");
        File searchDir = tempInputFolder.newFolder();
        runData.setSearchDirectory(searchDir.getAbsolutePath());
        Mockito.when(outputFactory.getOutput(Mockito.anyString())).thenCallRealMethod();
        controller.executeAnalysis(runData);
        Mockito.verify(outputFactory, Mockito.times(1)).getOutput(Mockito.anyString());
    }

    @Test
    public void testExecuteAnalysisHTMLOnly() throws IOException {
        RunData runData = new RunData();
        runData.getOutputFormats().add("html");
        File searchDir = tempInputFolder.newFolder();
        runData.setSearchDirectory(searchDir.getAbsolutePath());
        controller.executeAnalysis(runData);
        Mockito.verify(outputFactory, Mockito.times(1)).getOutput(Mockito.anyString());
    }

    @Test
    public void testExecuteAnalysisFileNotFound() throws IOException {
        RunData runData = new RunData();
        runData.getOutputFormats().add("csv");
        runData.getOutputFormats().add("html");
        runData.setSearchDirectory("fred");
        controller.executeAnalysis(runData);
        Mockito.verify(outputFactory, Mockito.times(0)).getOutput(Mockito.anyString());
    }

    @Test
    public void testExecuteAnalysisNotDirectory() throws IOException {
        RunData runData = new RunData();
        runData.getOutputFormats().add("csv");
        runData.getOutputFormats().add("html");
        File searchDir = tempInputFolder.newFile();
        runData.setSearchDirectory(searchDir.getAbsolutePath());
        controller.executeAnalysis(runData);
        Mockito.verify(outputFactory, Mockito.times(0)).getOutput(Mockito.anyString());
    }

}
