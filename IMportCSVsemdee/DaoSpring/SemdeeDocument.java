./**
 * Representation of a Document who return by a Crawl
 */
public class SemdeeDocument {
    private Long id;
    private String webId = "";
    private String title = "";
    private String description = "";
    private String shortDescription="";
    private String tags = "";
    private String author = "";
    private Date eDate;
    private String source;
    private String language = "";
    private Date fetchDate;
    private Long crawlId;
    private String url;
    private List<Long> idCategories;
    private Long sourceId;
    private String rgxSearchResult = "";
    
    
    private String websource = "";  // web source of the document: "figaro", 
                                    //"20 minute"
    private Double distance = 0.;   // distance of the doc from the centroid 
                                    //of the space
    private Double proximity = 0.; // for the cluster and category document proximity
    
    private Long clusterId; // to determine the clusterid
    
    public SemdeeDocument(){
        webId = "";
        title = "";
        description = "";
        shortDescription="";
        tags = "";
        author = "";
        source = "";
        language = "";
        websource = "";
        distance = 0.;
        proximity = 0.;
    }

// getters and setters
…
}
