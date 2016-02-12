package com.castlemon.maven.domain;

public class Usage implements Comparable<Usage> {

    /*
     * This class contains the fields captured when a usage of the specified artifact is found
     */

    /*
     * All the fields below refer to the "using" project, not the artifact
     */

    private String groupId;

    private String artifactId;

    private String version;

    private String packaging;

    private String parentGroupId;

    private String parentArtifactId;

    private String parentVersion;

    /*
     * All the fields below refer to the artifact being used
     */
    private String versionUsed;

    private String scope;

    public static String[] getCSVTitles() {
        String[] titles = { "Group", "Artifact", "Version", "Packaging", "Parent Group", "Parent Artifact",
                "Version Used", "Scope" };
        return titles;
    }

    public String[] getCSVString() {
        String[] fields = { groupId, artifactId, version, packaging, parentGroupId, parentArtifactId, versionUsed,
                scope };
        return fields;
    }

    public String getIdentifier() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.groupId);
        builder.append(":");
        builder.append(this.artifactId);
        builder.append(":");
        builder.append(this.version);
        return builder.toString();
    }

    public String getParentIdentifier() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.parentGroupId);
        builder.append(":");
        builder.append(this.parentArtifactId);
        builder.append(":");
        builder.append(this.parentVersion);
        return builder.toString();
    }

    public int compareTo(Usage other) {
        // order by group, then artifact, then version
        if (this.groupId.equals(other.getGroupId())) {
            if (this.artifactId.equals(other.getArtifactId())) {
                return this.version.compareTo(other.getVersion());
            }
            return this.artifactId.compareTo(other.getArtifactId());
        }
        return this.groupId.compareTo(other.getGroupId());
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

    public String getPackaging() {
        return packaging;
    }

    public void setPackaging(String packaging) {
        this.packaging = packaging;
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

    public String getParentVersion() {
        return parentVersion;
    }

    public void setParentVersion(String parentVersion) {
        this.parentVersion = parentVersion;
    }

    public String getVersionUsed() {
        return versionUsed;
    }

    public void setVersionUsed(String versionUsed) {
        this.versionUsed = versionUsed;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

}
