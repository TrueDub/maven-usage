package com.castlemon.maven.processing.index;

import org.springframework.stereotype.Component;

@Component
public class MavenIndexer {

    /*
     * private PlexusContainer plexusContainer;
     * 
     * private Indexer indexer;
     * 
     * private IndexUpdater indexUpdater;
     * 
     * private Wagon httpWagon;
     * 
     * private IndexingContext centralContext;
     * 
     * public void testRun() throws PlexusContainerException, ComponentLookupException,
     * ExistingLuceneIndexMismatchException, IllegalArgumentException, IOException { final DefaultContainerConfiguration
     * config = new DefaultContainerConfiguration(); this.plexusContainer = new DefaultPlexusContainer(config);
     * this.indexer = plexusContainer.lookup(Indexer.class); this.indexUpdater =
     * plexusContainer.lookup(IndexUpdater.class); // this.httpWagon = plexusContainer.lookup(Wagon.class, "http");
     * 
     * // Files where local cache is (if any) and Lucene Index should be located File centralLocalCache = new
     * File("target/central-cache"); File centralIndexDir = new File("target/central-index");
     * 
     * // Creators we want to use (search for fields it defines) List<IndexCreator> indexers = new
     * ArrayList<IndexCreator>(); indexers.add(plexusContainer.lookup(IndexCreator.class, "min"));
     * indexers.add(plexusContainer.lookup(IndexCreator.class, "jarContent"));
     * indexers.add(plexusContainer.lookup(IndexCreator.class, "maven-plugin"));
     * 
     * File baseDir = new File("C:\\Users\\gallagherji\\.m2\\repository");
     * 
     * // Create context for central repository index centralContext = indexer.createIndexingContext("local", "local",
     * baseDir, centralIndexDir, null, null, true, true, indexers);
     * 
     * // Update the index (incremental update will happen if this is not 1st run and files are not deleted) // This
     * whole block below should not be executed on every app start, but rather controlled by some configuration // since
     * this block will always emit at least one HTTP GET. Central indexes are updated once a week, but // other index
     * sources might have different index publishing frequency. // Preferred frequency is once a week. if (true) {
     * System.out.println("Updating Index..."); System.out.println(
     * "This might take a while on first run, so please be patient!"); // Create ResourceFetcher implementation to be
     * used with IndexUpdateRequest // Here, we use Wagon based one as shorthand, but all we need is a ResourceFetcher
     * implementation
     * 
     * TransferListener listener = new AbstractTransferListener() { public void transferStarted(TransferEvent
     * transferEvent) { System.out.print("  Downloading " + transferEvent.getResource().getName()); }
     * 
     * public void transferProgress(TransferEvent transferEvent, byte[] buffer, int length) { }
     * 
     * public void transferCompleted(TransferEvent transferEvent) { System.out.println(" - Done"); } }; ResourceFetcher
     * resourceFetcher = new WagonHelper.WagonFetcher(httpWagon, listener, null, null);
     * 
     * ResourceFetcher resourceFetcher = new DefaultIndexUpdater.FileFetcher(baseDir);
     * 
     * Date centralContextCurrentTimestamp = centralContext.getTimestamp(); IndexUpdateRequest updateRequest = new
     * IndexUpdateRequest(centralContext, resourceFetcher); IndexUpdateResult updateResult =
     * indexUpdater.fetchAndUpdateIndex(updateRequest); if (updateResult.isFullUpdate()) { System.out.println(
     * "Full update happened!"); } else if (updateResult.getTimestamp().equals(centralContextCurrentTimestamp)) {
     * System.out.println("No update needed, index is up to date!"); } else { System.out.println(
     * "Incremental update happened, change covered " + centralContextCurrentTimestamp + " - " +
     * updateResult.getTimestamp() + " period."); }
     * 
     * System.out.println(); }
     * 
     * }
     */

}
