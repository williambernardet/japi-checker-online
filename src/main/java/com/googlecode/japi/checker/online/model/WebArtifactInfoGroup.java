package com.googlecode.japi.checker.online.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.maven.index.ArtifactInfo;
import org.apache.maven.index.ArtifactInfoGroup;

public class WebArtifactInfoGroup {
	private String groupKey;
	private List<WebArtifactInfo> artifactInfos = new ArrayList<WebArtifactInfo>();
	public WebArtifactInfoGroup(ArtifactInfoGroup aig) {
		groupKey = aig.getGroupKey();
		for (ArtifactInfo ai : aig.getArtifactInfos()) {
			artifactInfos.add(new WebArtifactInfo(ai));
		}
	}
	
	public String getGroupKey() {
		return groupKey;
	}
	
	public List<WebArtifactInfo> getArtifactInfos() {
		return artifactInfos;
	}
	
	public static Map<String, WebArtifactInfoGroup> toWebArtifactInfoGroup(Map<String, ArtifactInfoGroup> data) {
		Map<String, WebArtifactInfoGroup> result = new TreeMap<String, WebArtifactInfoGroup>();
		for (Entry<String, ArtifactInfoGroup> e : data.entrySet()) {
			result.put(e.getKey(), new WebArtifactInfoGroup(e.getValue()));
		}
		return result;
	}
	
}
