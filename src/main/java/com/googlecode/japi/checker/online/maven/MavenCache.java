package com.googlecode.japi.checker.online.maven;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import org.apache.maven.index.ArtifactInfo;
import org.apache.commons.io.IOUtils;

public class MavenCache {
	private static final Logger LOGGER = Logger.getLogger(MavenCache.class.getName());
	private File cacheDir = new File("data" + File.separator + "maven-cache");
	
	public File retrieve(ArtifactInfo artifactInfo) throws ArtifactNotFound {
		LOGGER.info("retreive " + artifactInfo);
		cacheDir.mkdirs();
		File artifact = new File(cacheDir, artifactInfo.context + File.separator + artifactInfo.groupId + File.separator + artifactInfo.artifactId + "-" + artifactInfo.version + "." + artifactInfo.packaging);
		LOGGER.info("filename in cache " + artifact.getAbsolutePath());
		LOGGER.info("remoteUrl " + artifactInfo.remoteUrl);
		LOGGER.info("repository " + artifactInfo.repository);
		LOGGER.info("context " + artifactInfo.context);
		if (artifact.exists()) {
			return artifact;
		}
		artifact.getParentFile().mkdirs();
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(artifact);
			IOUtils.copy(new URL(artifactInfo.remoteUrl).openStream(), fos);
		} catch (MalformedURLException e) {
			IOUtils.closeQuietly(fos);
			artifact.delete();
			throw new ArtifactNotFound(e.getMessage(), e);
		} catch (IOException e) {
			IOUtils.closeQuietly(fos);
			artifact.delete();
			throw new ArtifactNotFound(e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(fos);
		}
		return artifact;
	}
	
}
