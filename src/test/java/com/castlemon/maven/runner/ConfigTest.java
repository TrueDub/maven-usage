package com.castlemon.maven.runner;

import org.junit.Assert;
import org.junit.Test;

public class ConfigTest {

    @Test
    public void testConfig() {
        Config config = new Config();
        Assert.assertTrue(config instanceof Config);
    }

}
