/**
 * Dao permettant la liaison entre le CrawlService et la base de donnée
 */
@Repository
public class CrawlDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(CrawlDao.class);

    private static final String REQUEST_ADD_DOCUMENT = "insert into document "
            + "(crawl_id, source_id, url, title, short_description, description, "
            + "tags, edate, author, language, fetch_date, web_id, distance, websource) "
            + "values (:crawl_id, :source_id, :url, :title, :shortdescription, "
            + ":description, :tags, :edate, :author, :language, now(), :web_id, "
+ ":distance, :websource)";

    @Autowired
    private NamedParameterJdbcTemplate semdeeTemplate;

    /**
     * ajoute les documents trouvés par le crawl en base
     *
     * @param crawlId
     * @param resultats
     * @param source
     * @throws SQLException
     */
    public void writeDocumentsInBdd(final Long crawlId, final List<SemdeeDocument> resultats, final Source source) throws SQLException {
        LOGGER.info("{CrawlDao::writeDocumentsInBdd} crawlId:{} - nbResultats:{} - sourceId:{}",
                crawlId, resultats.size(), source.getId());
        Map<String, Object> params = null;

        List<Map<String, Object>> paramArray = new LinkedList();
        Map<String, Object>[] paramArray2 = new Map[100];

        for (int i = 0; i < paramArray2.length; i++) {
            paramArray2[i] = null;
        }
        int index = 0;
        for (SemdeeDocument semdeeDocument : resultats) {
            params = new HashMap<String, Object>();
            params.put("crawl_id", crawlId);
            params.put("source_id", source.getId());
            params.put("url", semdeeDocument.getUrl());
            params.put("title", semdeeDocument.getTitle());
            params.put("description", semdeeDocument.getDescription());
            String shortd = semdeeDocument.getShortDescription();
            int length = shortd.length();
            int min = Math.min(253, length);
            params.put("shortdescription", semdeeDocument.getShortDescription().substring(0, min));

            params.put("tags", semdeeDocument.getTags());
            params.put("edate", semdeeDocument.geteDate());
            int lengthAuthor = semdeeDocument.getAuthor().length();
            min = Math.min(98, lengthAuthor);
            params.put("author", semdeeDocument.getAuthor().substring(0, min));
            params.put("language", semdeeDocument.getLanguage());
            params.put("web_id", semdeeDocument.getWebId());
            params.put("distance", semdeeDocument.getDistance());
            params.put("websource", semdeeDocument.getWebSource());
            //paramArray.add(params);
            paramArray2[index] = params;
            index++;
            if (index == paramArray2.length) {
                semdeeTemplate.batchUpdate(REQUEST_ADD_DOCUMENT, paramArray2);
                for (int i = 0; i < paramArray2.length; i++) {
                    paramArray2[i] = null;
                }
                index = 0;
            }
        }
        if (index > 0) {
            Map<String, Object>[] paramArray3 = new Map[index];
            for (int i = 0; i < index; i++) {
                paramArray3[i] = paramArray2[i];
            }
            semdeeTemplate.batchUpdate(REQUEST_ADD_DOCUMENT, paramArray3);
            paramArray.clear();
        }
    }

}

