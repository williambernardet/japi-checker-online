package com.googlecode.japi.checker.online.model;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.maven.index.ArtifactInfo;
import org.apache.maven.index.ArtifactInfoGroup;

public class WebArtifactInfoGroup {
	private String groupKey;
	private Set<WebArtifactInfo> artifactInfos = new HashSet<WebArtifactInfo>();
	public WebArtifactInfoGroup(ArtifactInfoGroup aig) {
		groupKey = aig.getGroupKey();
		for (ArtifactInfo ai : aig.getArtifactInfos()) {
			artifactInfos.add(new WebArtifactInfo(ai));
		}
	}
	
	public String getGroupKey() {
		return groupKey;
	}
	
	public Set<WebArtifactInfo> getArtifactInfos() {
		return artifactInfos;
	}
	
	public static Map<String, WebArtifactInfoGroup> toWebArtifactInfoGroup(Map<String, ArtifactInfoGroup> data) {
		Map<String, WebArtifactInfoGroup> result = new Hashtable<String, WebArtifactInfoGroup>();
		for (Entry<String, ArtifactInfoGroup> e : data.entrySet()) {
			result.put(e.getKey(), new WebArtifactInfoGroup(e.getValue()));
		}
		return result;
	}
	
}
