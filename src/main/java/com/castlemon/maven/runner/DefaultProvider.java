package com.castlemon.maven.runner;

import java.io.File;

import com.beust.jcommander.IDefaultProvider;

public class DefaultProvider implements IDefaultProvider {

    @Override
    public String getDefaultValueFor(String arg0) {
        if (arg0.equals("-searchDir")) {
            return System.getProperty("user.home") + File.separator + ".m2" + File.separator + "repository";
        }
        if (arg0.equals("-outputFormats")) {
            return "HTML";
        }
        if (arg0.equals("-repo")) {
            return System.getProperty("user.home") + File.separator + ".m2" + File.separator + "repository";
        }
        return null;
    }

}
