package com.castlemon.maven.processing;

import org.springframework.stereotype.Component;

@Component
public class ArtifactIndexingService {

    /*
     * private static final Logger logger = LoggerFactory.getLogger(ArtifactIndexingService.class);
     * 
     * @Autowired private RepositoryIndexManager repositoryIndexManager;
     * 
     * public SearchResults search(SearchRequest searchRequest) throws IOException, ParseException { SearchResults
     * searchResults = new SearchResults();
     * 
     * final String repositoryId = searchRequest.getRepository();
     * 
     * if (repositoryId != null && !repositoryId.isEmpty()) { logger.debug("Repository: {}", repositoryId);
     * 
     * final Map<String, Collection<ArtifactInfo>> resultsMap = getResultsMap(repositoryId, searchRequest.getQuery());
     * 
     * if (!resultsMap.isEmpty()) { searchResults.setResults(resultsMap); }
     * 
     * if (logger.isDebugEnabled()) { int results = resultsMap.entrySet().iterator().next().getValue().size();
     * 
     * logger.debug("Results: {}", results); } } else { Map<String, Collection<ArtifactInfo>> resultsMap = new
     * LinkedHashMap<String, Collection<ArtifactInfo>>(); for (String repoId :
     * repositoryIndexManager.getIndexes().keySet()) { logger.debug("Repository: {}", repoId);
     * 
     * final RepositoryIndexer repositoryIndex = repositoryIndexManager.getRepositoryIndex(repoId); if (repositoryIndex
     * != null) { final Set<ArtifactInfo> artifactInfoResults = repositoryIndexManager.getRepositoryIndex(repoId)
     * .search(searchRequest.getQuery());
     * 
     * if (!artifactInfoResults.isEmpty()) { resultsMap.put(repoId, artifactInfoResults); }
     * 
     * logger.debug("Results: {}", artifactInfoResults.size()); } }
     * 
     * searchResults.setResults(resultsMap); }
     * 
     * return searchResults; }
     * 
     * public boolean contains(SearchRequest searchRequest) throws IOException, ParseException { return
     * !getResultsMap(searchRequest.getRepository(), searchRequest.getQuery()).isEmpty(); }
     * 
     * public Map<String, Collection<ArtifactInfo>> getResultsMap(String repositoryId, String query) throws IOException,
     * ParseException { Map<String, Collection<ArtifactInfo>> resultsMap = new LinkedHashMap<String,
     * Collection<ArtifactInfo>>(); final Set<ArtifactInfo> artifactInfoResults =
     * repositoryIndexManager.getRepositoryIndex(repositoryId) .search(query);
     * 
     * if (!artifactInfoResults.isEmpty()) { resultsMap.put(repositoryId, artifactInfoResults); }
     * 
     * return resultsMap; }
     * 
     * public RepositoryIndexManager getRepositoryIndexManager() { return repositoryIndexManager; }
     * 
     * public void setRepositoryIndexManager(RepositoryIndexManager repositoryIndexManager) {
     * this.repositoryIndexManager = repositoryIndexManager; }
     */

}
