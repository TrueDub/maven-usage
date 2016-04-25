package com.castlemon.maven.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(separators = "=")
public class RunData {

    // input data

    @Parameter(names = { "-group" }, description = "group to be searched for", required = true)
    private String group;

    @Parameter(names = { "-artifact" }, description = "artifact to be searched for", required = true)
    private String artifact;

    @Parameter(names = {
            "-outputDir" }, description = "output directory - where the CSVs or reports will be written to", required = true)
    private String outputDirectory;

    @Parameter(names = {
            "-searchDir" }, description = "directory to be searched - part of a local repo, or a specific directory within a local repo (to narrow the search)")
    private String searchDirectory;

    @Parameter(names = "-outputFormats", description = "output format(s) - comma-separated list (currently HTML and CSV supported)")
    private List<String> outputFormats = new ArrayList<String>();

    @Parameter(names = { "-repo" }, description = "the Maven repo")
    private String repo;

    @Parameter(names = { "-help", "-options" }, description = "display help message", help = true)
    private String helpParameter;

    // run data

    private List<Usage> usages = new ArrayList<Usage>();

    private long executionTimeInMillis;

    // stats on poms read

    private int pomsRead;

    private int pomsProcessed;

    private int pomsReadError;

    private List<String> pomsInError = new ArrayList<String>();

    // stats on processed usages

    private Map<String, Integer> versionCounts = new TreeMap<String, Integer>();

    // **********************************************

    public boolean displayHelp() {
        if (helpParameter == null || helpParameter.isEmpty()) {
            return false;
        }
        return true;
    }

    public int getOccurrencesFound() {
        return usages.size();
    }

    public String getFormattedElapsedTime() {
        return String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(executionTimeInMillis),
                TimeUnit.MILLISECONDS.toSeconds(executionTimeInMillis)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(executionTimeInMillis)));
    }

    public void incrementPomsProcessed() {
        pomsProcessed++;
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

    public String getRepo() {
        return repo;
    }

    public void setRepo(String repo) {
        this.repo = repo;
    }

    public String getHelpParameter() {
        return helpParameter;
    }

    public void setHelpParameter(String helpParameter) {
        this.helpParameter = helpParameter;
    }

}
