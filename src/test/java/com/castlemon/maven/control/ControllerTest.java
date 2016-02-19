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
import com.castlemon.maven.output.CSVOutput;
import com.castlemon.maven.output.HTMLOutput;
import com.castlemon.maven.processing.PomProcessor;
import com.castlemon.maven.processing.StatsGenerator;

@RunWith(MockitoJUnitRunner.class)
public class ControllerTest {

    @Mock
    private PomProcessor pomProcessor;

    @Mock
    private StatsGenerator statsGenerator;

    @Mock
    private HTMLOutput htmlOutput;

    @Mock
    private CSVOutput csvOutput;

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
        Mockito.verify(csvOutput, Mockito.times(1)).writeCSVFile(Mockito.any(RunData.class));
        Mockito.verify(htmlOutput, Mockito.times(1)).writeHTMLOutput(Mockito.any(RunData.class));
    }

    @Test
    public void testExecuteAnalysisCSVOnly() throws IOException {
        RunData runData = new RunData();
        runData.getOutputFormats().add("csv");
        File searchDir = tempInputFolder.newFolder();
        runData.setSearchDirectory(searchDir.getAbsolutePath());
        controller.executeAnalysis(runData);
        Mockito.verify(csvOutput, Mockito.times(1)).writeCSVFile(Mockito.any(RunData.class));
        Mockito.verify(htmlOutput, Mockito.times(0)).writeHTMLOutput(Mockito.any(RunData.class));
    }

    @Test
    public void testExecuteAnalysisHTMLOnly() throws IOException {
        RunData runData = new RunData();
        runData.getOutputFormats().add("html");
        File searchDir = tempInputFolder.newFolder();
        runData.setSearchDirectory(searchDir.getAbsolutePath());
        controller.executeAnalysis(runData);
        Mockito.verify(csvOutput, Mockito.times(0)).writeCSVFile(Mockito.any(RunData.class));
        Mockito.verify(htmlOutput, Mockito.times(1)).writeHTMLOutput(Mockito.any(RunData.class));
    }

    @Test
    public void testExecuteAnalysisFileNotFound() throws IOException {
        RunData runData = new RunData();
        runData.getOutputFormats().add("csv");
        runData.getOutputFormats().add("html");
        runData.setSearchDirectory("fred");
        controller.executeAnalysis(runData);
        Mockito.verify(csvOutput, Mockito.times(0)).writeCSVFile(Mockito.any(RunData.class));
        Mockito.verify(htmlOutput, Mockito.times(0)).writeHTMLOutput(Mockito.any(RunData.class));
    }

    @Test
    public void testExecuteAnalysisNotDirectory() throws IOException {
        RunData runData = new RunData();
        runData.getOutputFormats().add("csv");
        runData.getOutputFormats().add("html");
        File searchDir = tempInputFolder.newFile();
        runData.setSearchDirectory(searchDir.getAbsolutePath());
        controller.executeAnalysis(runData);
        Mockito.verify(csvOutput, Mockito.times(0)).writeCSVFile(Mockito.any(RunData.class));
        Mockito.verify(htmlOutput, Mockito.times(0)).writeHTMLOutput(Mockito.any(RunData.class));
    }

}
