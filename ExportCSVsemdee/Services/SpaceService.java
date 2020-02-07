SpaceService.java{
    @Autowired
    private SpaceDao spaceDao;

    @Autowired
    private CrawlDao crawlDao;
    
    /**
     * Read documents from crawl table in database, convert into CSV and write 
     * it into output as stream.
     * This is the most efficient one in terms of performance, due to the 
     * buffered like treatment of the converting documents to CSV string process.
     * @param os
     * @param spaceId
     * @param userId
     * @throws IOException 
     */
    public void readAndWrite2(OutputStream os, Long spaceId, Long userId)
			throws IOException {
        byte[] data = new byte[2048];
        int read = 0;
        int count = 0;
  
        int size = 100;
        int docCount = 0;

        List<SemdeeDocument> docs = getAllDocsBySpaceId(spaceId);
        String result = "";
        String header = "\"id\";\"title\";\"tags\";\"description\";\"author\";"
               + "\"edit date\";\"language\";\"url\";\"source\";\"crawl id\";\"crawl label\"\n";
        result += header;
        SpaceDo space = spaceDao.getSpace(spaceId, userId);
        Map<Long, String> crawlIdLabelMap = new HashMap();
        for (Long crawlId: space.getCrawlsId()){
            String label = crawlDao.getCrawl(crawlId, userId).getQuery();
            crawlIdLabelMap.put(crawlId, label);
        }
        for (SemdeeDocument doc:docs){
            result += doc.toCSV() + doc.getCrawlId()+ ";\"" +crawlIdLabelMap.get(doc.getCrawlId()) + "\"\n";
            docCount ++;
            if (docCount % 10000 == 0)
                LOGGER.info("converted {} document to csv", docCount);
            if (docCount % size == 0){
                
                InputStream is = new ByteArrayInputStream(result.getBytes(StandardCharsets.UTF_8));
                while ((read = is.read(data)) > 0) {
                    os.write(data, 0, read);
                    count++;
                    if (count % 512 == 0)
                        LOGGER.info("have written {} mb", count/512);
                }
                result = "";
            }

        }
        
        if (result.length() >0 ){
            InputStream is = new ByteArrayInputStream(result.getBytes(StandardCharsets.UTF_8));
            while ((read = is.read(data)) > 0) {
                os.write(data, 0, read);
                count++;
                if (count % 512 == 0)
                    LOGGER.info("have written {} mb", count/512);
            }
        }

        LOGGER.info("finished writing");
        os.flush();
    }


    /**
     * Get all the docs from a space.
     */
    public List<SemdeeDocument> getAllDocsBySpaceId(final Long spaceId) {
        List<SemdeeDocument> docs=spaceDao.findDocumentsBySpaceId(spaceId);
        LOGGER.info("docs size=>{}",docs.size());
        return docs;
    }

}

