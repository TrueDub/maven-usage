package com.castlemon.maven.output;

import org.junit.Assert;
import org.junit.Test;

import com.castlemon.maven.output.impl.CSVOutput;
import com.castlemon.maven.output.impl.HTMLOutput;

public class OutputFactoryTest {

    @Test
    public void testGetOutputCSV() {
        OutputFactory outputFactory = new OutputFactory();
        Output output = outputFactory.getOutput("CSV");
        Assert.assertTrue(output instanceof CSVOutput);
        Assert.assertFalse(output instanceof HTMLOutput);
    }

    @Test
    public void testGetOutputHTML() {
        OutputFactory outputFactory = new OutputFactory();
        Output output = outputFactory.getOutput("html");
        Assert.assertFalse(output instanceof CSVOutput);
        Assert.assertTrue(output instanceof HTMLOutput);
    }

    @Test
    public void testGetOutputNull() {
        OutputFactory outputFactory = new OutputFactory();
        Assert.assertNull(outputFactory.getOutput("fred"));
    }

}
