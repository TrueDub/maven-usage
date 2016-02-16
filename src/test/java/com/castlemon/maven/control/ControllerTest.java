package com.castlemon.maven.control;

import java.io.File;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.castlemon.maven.domain.RunData;
import com.castlemon.maven.processing.PomProcessor;
import com.castlemon.maven.processing.StatsGenerator;

@RunWith(MockitoJUnitRunner.class)
public class ControllerTest {

    @Mock
    private PomProcessor pomProcessor;

    @Mock
    private StatsGenerator statsGenerator;

    @Rule
    public TemporaryFolder tempInputFolder = new TemporaryFolder();

    @InjectMocks
    private Controller controller = new Controller();

    @Test
    public void testExecuteAnalysis() throws IOException {
        RunData runData = new RunData();
        File searchDir = tempInputFolder.newFolder();
        runData.setSearchDirectory(searchDir.getAbsolutePath());
        controller.executeAnalysis(runData);

    }

}
