package com.castlemon.maven.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RunData {

    // input data
    private String group;

    private String artifact;

    private String searchDirectory;

    private List<String> outputFormats = new ArrayList<String>();

    private String outputDirectory;

    // run data

    private List<Usage> usages = new ArrayList<Usage>();

    private long executionTimeInMillis;

    // stats on poms read

    private int pomsRead;

    private int pomsProcessed;

    private int pomsNotProcessed;

    private List<String> pomsNotToBeProcessed = new ArrayList<String>();

    private int pomsReadError;

    private List<String> pomsInError = new ArrayList<String>();

    // stats on processed usages

    Map<String, Integer> versionCounts;

    // **********************************************

    public String getFormattedElapsedTime() {
        return String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(executionTimeInMillis),
                TimeUnit.MILLISECONDS.toSeconds(executionTimeInMillis)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(executionTimeInMillis)));
    }

    public void incrementPomsProcessed() {
        pomsProcessed++;
    }

    public void incrementPomsNotProcessed() {
        pomsNotProcessed++;
    }

    public void incrementPomsReadError() {
        pomsReadError++;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getArtifact() {
        return artifact;
    }

    public void setArtifact(String artifact) {
        this.artifact = artifact;
    }

    public String getSearchDirectory() {
        return searchDirectory;
    }

    public void setSearchDirectory(String searchDir) {
        this.searchDirectory = searchDir;
    }

    public List<String> getOutputFormats() {
        return outputFormats;
    }

    public void setOutputFormats(List<String> outputFormats) {
        this.outputFormats = outputFormats;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public List<Usage> getUsages() {
        return usages;
    }

    public void setUsages(List<Usage> usages) {
        this.usages = usages;
    }

    public long getExecutionTimeInMillis() {
        return executionTimeInMillis;
    }

    public void setExecutionTimeInMillis(long executionTimeInMillis) {
        this.executionTimeInMillis = executionTimeInMillis;
    }

    public int getPomsRead() {
        return pomsRead;
    }

    public void setPomsRead(int pomsRead) {
        this.pomsRead = pomsRead;
    }

    public int getPomsProcessed() {
        return pomsProcessed;
    }

    public void setPomsProcessed(int pomsProcessed) {
        this.pomsProcessed = pomsProcessed;
    }

    public int getPomsReadError() {
        return pomsReadError;
    }

    public void setPomsReadError(int pomsReadError) {
        this.pomsReadError = pomsReadError;
    }

    public List<String> getPomsInError() {
        return pomsInError;
    }

    public void setPomsInError(List<String> pomsInError) {
        this.pomsInError = pomsInError;
    }

    public Map<String, Integer> getVersionCounts() {
        return versionCounts;
    }

    public void setVersionCounts(Map<String, Integer> versionCounts) {
        this.versionCounts = versionCounts;
    }

    public int getPomsNotProcessed() {
        return pomsNotProcessed;
    }

    public void setPomsNotProcessed(int pomsNotProcessed) {
        this.pomsNotProcessed = pomsNotProcessed;
    }

    public List<String> getPomsNotToBeProcessed() {
        return pomsNotToBeProcessed;
    }

    public void setPomsNotToBeProcessed(List<String> pomsNotToBeProcessed) {
        this.pomsNotToBeProcessed = pomsNotToBeProcessed;
    }

}
