package com.castlemon.maven.domain;

public class Usage {

    /*
     * This class contains the fields captured when a usage of the specified artifact is found
     */

    /*
     * All the fields below refer to the "using" project, not the artifact
     */

    private String groupId;

    private String artifactId;

    private String version;

    private String parentGroupId;

    private String parentArtifactId;

    /*
     * All the fields below refer to the artifact being used
     */
    private String versionUsed;

    private String classifier;

    private String scope;

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getParentGroupId() {
        return parentGroupId;
    }

    public void setParentGroupId(String parentGroupId) {
        this.parentGroupId = parentGroupId;
    }

    public String getParentArtifactId() {
        return parentArtifactId;
    }

    public void setParentArtifactId(String parentArtifactId) {
        this.parentArtifactId = parentArtifactId;
    }

    public String getVersionUsed() {
        return versionUsed;
    }

    public void setVersionUsed(String versionUsed) {
        this.versionUsed = versionUsed;
    }

    public String getClassifier() {
        return classifier;
    }

    public void setClassifier(String classifier) {
        this.classifier = classifier;
    }

}
