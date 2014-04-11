package com.googlecode.japi.checker.online.maven;

public class ArtifactNotFound extends Exception {

	private static final long serialVersionUID = 1403018052668876272L;
	
	public ArtifactNotFound(String message) {
		super(message);
	}

	public ArtifactNotFound(String message, Throwable cause) {
		super(message, cause);
	}
}
