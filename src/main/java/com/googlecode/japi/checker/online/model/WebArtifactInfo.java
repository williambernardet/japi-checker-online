package com.googlecode.japi.checker.online.model;


import org.apache.maven.index.ArtifactInfo;

public class WebArtifactInfo {
	private ArtifactInfo ai;
	public WebArtifactInfo(ArtifactInfo ai) {
		this.ai = ai;
	}

	public String getGroupId() {
		return ai.groupId;
	}

	public String getArtifactId() {
		return ai.artifactId;
	}
	
	public String getVersion() {
		return ai.version;
	}

	public String getFextension() {
		return ai.fextension;
	}
	
	public String getPackaging() {
		return ai.packaging;
	}
}
