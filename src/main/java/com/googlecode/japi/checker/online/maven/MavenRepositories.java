package com.googlecode.japi.checker.online.maven;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;

public class MavenRepositories {

	private static MavenRepositories self;
	private Map<String, String> repositories = new Hashtable<String, String>();

	public MavenRepositories() {
		repositories.put("central", "http://repo1.maven.org/maven2");
	}
	public Map<String, String> getRepositories() {
		return Collections.unmodifiableMap(repositories);
	}

	public static MavenRepositories getMavenRepositories() {
		synchronized (MavenRepositories.class) {
			if (self == null) {
				self = new MavenRepositories();
			}
		}
		return self;
	}
}
