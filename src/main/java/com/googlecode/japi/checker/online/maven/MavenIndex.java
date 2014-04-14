package com.googlecode.japi.checker.online.maven;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.codehaus.plexus.DefaultPlexusContainer;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.PlexusContainerException;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.apache.commons.io.IOUtils;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.maven.index.ArtifactInfo;
import org.apache.maven.index.ArtifactInfoGroup;
import org.apache.maven.index.Field;
import org.apache.maven.index.GroupedSearchRequest;
import org.apache.maven.index.GroupedSearchResponse;
import org.apache.maven.index.Indexer;
import org.apache.maven.index.IteratorSearchRequest;
import org.apache.maven.index.IteratorSearchResponse;
import org.apache.maven.index.MAVEN;
import org.apache.maven.index.context.ExistingLuceneIndexMismatchException;
import org.apache.maven.index.context.IndexCreator;
import org.apache.maven.index.context.IndexingContext;
import org.apache.maven.index.expr.SourcedSearchExpression;
import org.apache.maven.index.search.grouping.GAGrouping;
import org.apache.maven.index.updater.IndexUpdateRequest;
import org.apache.maven.index.updater.IndexUpdateResult;
import org.apache.maven.index.updater.IndexUpdater;
import org.apache.maven.index.updater.ResourceFetcher;
import org.apache.maven.index.updater.WagonHelper;
import org.apache.maven.wagon.Wagon;
import org.apache.maven.wagon.events.TransferEvent;
import org.apache.maven.wagon.events.TransferListener;
import org.apache.maven.wagon.observers.AbstractTransferListener;

public class MavenIndex {
    private static final Logger LOGGER = Logger.getLogger(MavenIndex.class.getName());
    private final PlexusContainer plexusContainer;
        
    // The nexus indexer
    private Indexer indexer;

    //
    private final Wagon httpWagon;
    private final IndexUpdater indexUpdater;
    private IndexingContext centralContext;
    private boolean ready;
    
    public MavenIndex() throws ComponentLookupException, PlexusContainerException, ExistingLuceneIndexMismatchException, IllegalArgumentException, IOException {
        this.plexusContainer = new DefaultPlexusContainer();

        // lookup the indexer components from plexus
        this.indexer = plexusContainer.lookup(Indexer.class);
        this.indexUpdater = plexusContainer.lookup(IndexUpdater.class);
        // lookup wagon used to remotely fetch index
        this.httpWagon = plexusContainer.lookup(Wagon.class, "http");
        
        File dataDir = new File("./data");//new File(System.getProperty("jboss.server.data.dir"));

        // Creators we want to use (search for fields it defines)
        List<IndexCreator> indexers = new ArrayList<IndexCreator>();
        indexers.add(plexusContainer.lookup( IndexCreator.class, "min"));
        indexers.add(plexusContainer.lookup( IndexCreator.class, "jarContent"));
        indexers.add(plexusContainer.lookup( IndexCreator.class, "maven-plugin"));

        // Create context for central repository index
        for (Entry<String, String> repo : MavenRepositories.getMavenRepositories().getRepositories().entrySet()) {
            File indexDir = new File(dataDir, repo.getKey() +"/index");
            File cacheDir = new File(dataDir, repo.getKey() + "/cache");
            indexDir.mkdirs();
            cacheDir.mkdirs();
            this.centralContext = this.indexer.createIndexingContext(repo.getKey() + "-context", repo.getKey(), cacheDir, indexDir,
            		repo.getValue(), null, true, true, indexers);
        }

        Thread t = new Thread() {
            public void run()  {
                LOGGER.info( "Updating Index..." );
                LOGGER.info( "This might take a while on first run, so please be patient!" );
                // Create ResourceFetcher implementation to be used with IndexUpdateRequest
                // Here, we use Wagon based one as shorthand, but all we need is a ResourceFetcher implementation
                TransferListener listener = new AbstractTransferListener()
                {
                    int size;
                    int mega = 0;
                    public void transferStarted(TransferEvent transferEvent )
                    {
                        LOGGER.info( "Downloading " + transferEvent.getResource().getName() );
                        size = 0;
                        mega = 0;
                    }

                    public void transferProgress(TransferEvent transferEvent, byte[] buffer, int length)
                    {
                        size += length;
                        if (size/(1024*1024) > mega) {
                            mega = size/(1024*1024);
                            LOGGER.info( "Downloaded " + transferEvent.getResource().getName() + " " + mega + "MB");
                        }
                    }

                    public void transferCompleted(TransferEvent transferEvent)
                    {
                        LOGGER.info("Done");
                    }
                };
                ResourceFetcher resourceFetcher = new WagonHelper.WagonFetcher(httpWagon, listener, null, null);
                Date centralContextCurrentTimestamp = centralContext.getTimestamp();
                IndexUpdateRequest updateRequest = new IndexUpdateRequest(centralContext, resourceFetcher);
                try {
                    LOGGER.info("fetchAndUpdateIndex");
                    IndexUpdateResult updateResult = indexUpdater.fetchAndUpdateIndex(updateRequest);
                    if (updateResult.isFullUpdate())
                    {
                        LOGGER.info("Full update happened!");
                    }
                    else if (updateResult.getTimestamp().equals(centralContextCurrentTimestamp))
                    {
                        LOGGER.info("No update needed, index is up to date!");
                    }
                    else
                    {
                        LOGGER.info("Incremental update happened, change covered " + centralContextCurrentTimestamp
                            + " - " + updateResult.getTimestamp() + " period.");
                    }
                    ready = true;
                } catch (IOException e) {
                    LOGGER.severe(e.getMessage());
                }
            }
          };
          if (Boolean.parseBoolean(System.getProperty("com.googlecode.japi.checker.online.maven.updateIndex", "true"))) {
              t.start();
          } else {
              ready = true;
          }
    }
    
    public Indexer getIndexer() {
    	return indexer;
    }
    
    public List<ArtifactInfo> search(Query query) throws IOException {
        List<ArtifactInfo> result = new ArrayList<ArtifactInfo>();
        if (ready) {
            final IteratorSearchRequest request = new IteratorSearchRequest(query, Collections.singletonList(this.centralContext));
            
            IteratorSearchResponse response = null;
            try {
            	response = indexer.searchIterator(request);
            	for ( ArtifactInfo ai : response ) {
            		result.add(new ArtifactInfoWrapper(ai));
            	}
            } finally {
            	IOUtils.closeQuietly(response);
            }
        }
        return result;
    }

    public List<ArtifactInfo> search(String query) throws IOException {
        return this.search(Utils.toQuery(indexer, query));
    }

    public ArtifactInfo getArtifactInfo(String groupId, String artifactId, String version, String packaging) throws IOException, ArtifactNotFound {
    	BooleanQuery q = new BooleanQuery();
    	q.add(this.getIndexer().constructQuery(MAVEN.GROUP_ID, new SourcedSearchExpression(groupId)), Occur.MUST);
    	q.add(this.getIndexer().constructQuery(MAVEN.ARTIFACT_ID, new SourcedSearchExpression(artifactId)), Occur.MUST);
    	q.add(this.getIndexer().constructQuery(MAVEN.VERSION, new SourcedSearchExpression(version)), Occur.MUST);
    	q.add(this.getIndexer().constructQuery(MAVEN.PACKAGING, new SourcedSearchExpression(packaging)), Occur.MUST);
        q.add(this.getIndexer().constructQuery(MAVEN.CLASSIFIER, new SourcedSearchExpression(Field.NOT_PRESENT)), Occur.MUST_NOT);
        List<ArtifactInfo> result = this.search(q);
        if (result.size() == 1) {
        	return result.get(0);
        }
        throw new ArtifactNotFound("Could not find artifact " + groupId + ":" + artifactId + ":" + version + ":" + packaging);
    }
    
    public Map<String, ArtifactInfoGroup> searchGroupByArtifacts(String query, boolean exactMatch) throws IOException {
        if (ready) {
            Query q = Utils.toQuery(indexer, query, exactMatch);
            GroupedSearchResponse response = null;
            try {
            	response = indexer.searchGrouped(new GroupedSearchRequest(q, new GAGrouping(), this.centralContext));
                return response.getResults();
            } finally {
            	IOUtils.closeQuietly(response);
            }
        }
        return new HashMap<String, ArtifactInfoGroup>();
    }

    public static class ArtifactInfoWrapper extends ArtifactInfo {
		private static final long serialVersionUID = 3899873549801889543L;

		public ArtifactInfoWrapper(ArtifactInfo ai) {
    		super(ai.repository, ai.groupId, ai.artifactId, ai.version, ai.classifier);
    		this.packaging = ai.packaging;
    		this.name = ai.name;
    		this.sha1 = ai.sha1;
    		this.fextension = ai.fextension;
    		this.fname = ai.fname;
    		this.repository = ai.repository;
    		this.remoteUrl = ai.remoteUrl;
    		if (this.remoteUrl == null) {
    			this.remoteUrl = MavenRepositories.getMavenRepositories().getRepositories().get(ai.repository);
    			this.remoteUrl += "/" + ai.groupId.replace('.', '/') + "/" + ai.artifactId + "/" +
    					ai.version +  "/" + 
    					ai.artifactId + "-" + ai.version +
    					(ai.classifier != null? "-" + ai.classifier : "") + 
    					(ai.packaging != null? "." + ai.packaging : "");
    		}
    		this.context = ai.context;
    		this.description = ai.description;
    	}
    	
    	public String getVersion() {
    		return this.version;
    	}

    	public String getGroupId() {
    		return this.groupId;
    	}

    	public String getArtifactId() {
    		return this.groupId;
    	}
    }
    
}