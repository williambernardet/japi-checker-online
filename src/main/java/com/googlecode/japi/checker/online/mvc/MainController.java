package com.googlecode.japi.checker.online.mvc;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.maven.index.ArtifactInfo;
import org.apache.maven.index.Field;
import org.apache.maven.index.MAVEN;
import org.apache.maven.index.expr.SourcedSearchExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.googlecode.japi.checker.online.maven.MavenCache;
import com.googlecode.japi.checker.online.maven.MavenIndex;
import com.googlecode.japi.checker.online.model.WebArtifactInfoGroup;
import com.googlecode.japi.checker.online.model.WebReport;
import com.googlecode.japi.checker.BCChecker;
import com.googlecode.japi.checker.Reporter;
import com.googlecode.japi.checker.Reporter.Report;

@Controller
@RequestMapping(value="/")
public class MainController
{
    private static final Logger LOGGER = Logger.getLogger(MainController.class.getName());
    
    @Autowired
    private MavenIndex mavenIndex;

    @Autowired
    private MavenCache mavenCache;
    
    /**
     * Needed to load index.jsp....
     * @return
     */
    @RequestMapping(method=RequestMethod.GET)
    public String getIndex() {
        return "index";
    }

    /**
     * 
     * @param query
     * @return
     */
    @RequestMapping(value="search", method=RequestMethod.GET, produces="application/json")
    public @ResponseBody Map<String, WebArtifactInfoGroup> getSearchResult(@RequestParam(value="query", required=true) String query,
    		@RequestParam(value="exactmatch", required=true) boolean exactMatch) {
        LOGGER.info("query=" + query);
        LOGGER.info("exactMatch=" + exactMatch);
        try {
            return WebArtifactInfoGroup.toWebArtifactInfoGroup(mavenIndex.searchGroupByArtifacts(query, exactMatch));
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
            return new Hashtable<String, WebArtifactInfoGroup>();
        }
    }


    @RequestMapping(value="diff/{groupId}/{artifactId}/{version}/{extension}", method=RequestMethod.GET)
    public String getDiff(@PathVariable String groupId, @PathVariable String artifactId,
    		@PathVariable String version, @PathVariable String extension, Model model) {
        try {
        	LOGGER.info("groupId=" + groupId);
        	LOGGER.info("artifactId=" + artifactId);
        	LOGGER.info("version=" + version);
        	LOGGER.info("extension=" + extension);
        	model.addAttribute("groupId", groupId);
        	model.addAttribute("artifactId", artifactId);
        	model.addAttribute("version", version);
        	model.addAttribute("extension", extension);
        	BooleanQuery q = new BooleanQuery();
        	q.add(mavenIndex.getIndexer().constructQuery(MAVEN.GROUP_ID, new SourcedSearchExpression(groupId)), Occur.MUST);
        	q.add(mavenIndex.getIndexer().constructQuery(MAVEN.ARTIFACT_ID, new SourcedSearchExpression(artifactId)), Occur.MUST);
        	q.add(mavenIndex.getIndexer().constructQuery(MAVEN.VERSION, new SourcedSearchExpression(version)), Occur.MUST_NOT);
        	q.add(mavenIndex.getIndexer().constructQuery(MAVEN.PACKAGING, new SourcedSearchExpression(extension)), Occur.MUST);
            q.add(mavenIndex.getIndexer().constructQuery(MAVEN.CLASSIFIER, new SourcedSearchExpression(Field.NOT_PRESENT)), Occur.MUST_NOT);
        	model.addAttribute("artifacts", mavenIndex.search(q));
        	return "diff";
    	} catch (IOException e) {
    		LOGGER.severe(e.getMessage());
    		return "error";
    	}
    }

    @RequestMapping(value="diff/{groupId}/{artifactId}/{version:.+}/{extension}/{versionAgainst:.+}", method=RequestMethod.GET)
    public String getDiffAgainst(@PathVariable String groupId, @PathVariable String artifactId,
    		@PathVariable String version, @PathVariable String extension, @PathVariable String versionAgainst, Model model) {
        try {
        	LOGGER.info("groupId=" + groupId);
        	LOGGER.info("artifactId=" + artifactId);
        	LOGGER.info("version=" + version);
        	LOGGER.info("extension=" + extension);
        	LOGGER.info("versionAgainst=" + versionAgainst);
        	model.addAttribute("groupId", groupId);
        	model.addAttribute("artifactId", artifactId);
        	model.addAttribute("version", version);
        	model.addAttribute("extension", extension);
        	model.addAttribute("versionAgainst", versionAgainst);
        	ArtifactInfo referenceInfo = mavenIndex.getArtifactInfo(groupId, artifactId, version, extension);
        	ArtifactInfo againstInfo = mavenIndex.getArtifactInfo(groupId, artifactId, versionAgainst, extension);
        	LOGGER.info("Retrieving artifacts...");
        	File referenceArtifact = mavenCache.retrieve(referenceInfo);
        	File againstArtifact = mavenCache.retrieve(againstInfo);
        	BCChecker checker = new BCChecker();
        	final List<Report> reports = new ArrayList<Report>();
        	checker.setReporter(new Reporter() {

				@Override
				public void report(Report report) {
					reports.add(report);
				}
        		
        	});
        	checker.checkBacwardCompatibility(referenceArtifact, againstArtifact);
        	model.addAttribute("reports", reports);
            return "diff";
    	} catch (Exception e) {
    		LOGGER.severe(e.getMessage());
    		return "error";
    	}
    }
    
    @RequestMapping(value="diff/{groupId}/{artifactId}/{version:.+}/{extension}/{versionAgainst:.+}", method=RequestMethod.GET, produces="application/json")
    public @ResponseBody List<WebReport> getDiffAgainstAsJson(@PathVariable String groupId, @PathVariable String artifactId,
    		@PathVariable String version, @PathVariable String extension, @PathVariable String versionAgainst) throws IOException {
        try {
        	ArtifactInfo referenceInfo = mavenIndex.getArtifactInfo(groupId, artifactId, version, extension);
        	ArtifactInfo againstInfo = mavenIndex.getArtifactInfo(groupId, artifactId, versionAgainst, extension);
        	LOGGER.info("Retrieving artifacts...");
        	File referenceArtifact = mavenCache.retrieve(referenceInfo);
        	File againstArtifact = mavenCache.retrieve(againstInfo);
        	BCChecker checker = new BCChecker();
        	final List<WebReport> reports = new ArrayList<WebReport>();
        	checker.setReporter(new Reporter() {

				@Override
				public void report(Report report) {
					reports.add(new WebReport(report));
				}
        		
        	});
        	checker.checkBacwardCompatibility(referenceArtifact, againstArtifact);
        	return reports;
    	} catch (Exception e) {
    		LOGGER.severe(e.getMessage());
    		return new ArrayList<WebReport>();
    	}
    }
}
