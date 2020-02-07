SpaceDao.java{
   
    private static final String FIND_DOCS_BY_SPACEID
            = "select d.id, d.crawl_id, d.title, "
            + "s.source, d.description, d.short_description, d.language,d.url, "
            + "d.tags, d.fetch_date, d.author, d.distance, d.websource "
            + "from document d "
            + "inner join crawl c on d.crawl_id=c.id "
            + "inner join space_crawl sc on sc.crawl_id=c.id "
            + "inner join source s on d.source_id = s.id "
            + "where sc.space_id=:spaceId";

    private static final String FIND_CATEGORY_OF_DOCS = "select category_id from document_category dc where document_id=:docid";

   /**
    * Get the docs by space id
    */
   public List<SemdeeDocument> findDocumentsBySpaceId(final Long spaceId) {
        LOGGER.info("{SpaceDo::findDocumentsBySpaceId} spaceId:{}", spaceId);
        Map<String, Object> params = new HashMap<String, Object>(1);
        params.put("spaceId", spaceId);

        List<SemdeeDocument> documents;

        try {
            documents = (List<SemdeeDocument>) semdeeTemplate.query(FIND_DOCS_BY_SPACEID, params, new BeanPropertyRowMapper(SemdeeDocument.class));
            LOGGER.info("docs->{}", documents.size());
            for (SemdeeDocument doc : documents) {
                params.clear();
                params.put("docid", doc.getId());
                doc.setIdCategories(semdeeTemplate.queryForList(FIND_CATEGORY_OF_DOCS, params, Long.class));
            }
        } catch (Exception e) {
            LOGGER.error("error:" + e.getMessage(), e);
            documents = new ArrayList<SemdeeDocument>();
        }

        return documents;
    }

}
