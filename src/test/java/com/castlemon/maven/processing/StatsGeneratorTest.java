package com.castlemon.maven.processing;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.castlemon.maven.domain.RunData;
import com.castlemon.maven.domain.Usage;

public class StatsGeneratorTest {

    @Test
    public void testGenerateStats() {
        Usage usage1 = new Usage();
        usage1.setVersionUsed("1.0");
        Usage usage2 = new Usage();
        usage2.setVersionUsed("1.0");
        Usage usage3 = new Usage();
        usage3.setVersionUsed("1.1");
        Usage usage4 = new Usage();
        usage4.setVersionUsed("1.2");
        List<Usage> usages = new ArrayList<Usage>();
        usages.add(usage1);
        usages.add(usage2);
        usages.add(usage3);
        usages.add(usage4);
        RunData runData = new RunData();
        runData.setUsages(usages);
        StatsGenerator generator = new StatsGenerator();
        generator.generateStats(runData);
        Assert.assertEquals(3, runData.getVersionCounts().size());
        Assert.assertEquals(new Integer(2), runData.getVersionCounts().get("1.0"));
        Assert.assertEquals(new Integer(1), runData.getVersionCounts().get("1.1"));
        Assert.assertEquals(new Integer(1), runData.getVersionCounts().get("1.2"));
    }

}
