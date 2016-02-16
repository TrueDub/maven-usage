package com.castlemon.maven.domain;

import org.agileware.test.PropertiesTester;
import org.junit.Assert;
import org.junit.Test;

public class RunDataTest {

    @Test
    public void test() throws Exception {
        PropertiesTester tester = new PropertiesTester();
        tester.testAll(RunData.class);
    }

    @Test
    public void testOther() {
        RunData runData = new RunData();
        Assert.assertEquals(0, runData.getOccurrencesFound());
        //
        Assert.assertEquals(0, runData.getPomsProcessed());
        runData.incrementPomsProcessed();
        Assert.assertEquals(1, runData.getPomsProcessed());
        //
        Assert.assertEquals(0, runData.getPomsReadError());
        runData.incrementPomsReadError();
        Assert.assertEquals(1, runData.getPomsReadError());
        //
        runData.setExecutionTimeInMillis(1234567l);
        Assert.assertEquals("20 min, 34 sec", runData.getFormattedElapsedTime());
    }

}
