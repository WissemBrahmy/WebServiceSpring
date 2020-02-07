
/**
 * Service s'occupant des operations autour du Crawl
 */
public class CrawlService {
    @Autowired
    private CrawlDao crawlDao;
…………… 
    /**
     * Upload a csv file.
     * @param bytes
     * @param crawlId
     * @param userId
     * @return
     */
    @Transactional
    public boolean uploadFile(byte[] bytes, Long crawlId, Long userId) {
        LOGGER.info("{CrawlService::uploadFile} crawlId:{} - userId:{} "
                + "- fileLength:{}", crawlId, userId, bytes.length);
        try {
            Long begin = System.currentTimeMillis();
            Crawl crawlDo = crawlDao.getCrawl(crawlId, userId);
            if (crawlDo == null) {
                LOGGER.info("crawlId={} -> specified crawl does not exists or is attached to another user", crawlId);
                return false;
            }

            Long duration = crawlDo.getDuration();
            List<SemdeeDocument> docs = new ArrayList<SemdeeDocument>();
            docs = parseCSV(bytes, crawlId);
            Source source = new Source();
            source.setId(3L);
            crawlDao.writeDocumentsInBdd(crawlId, docs, source);
            Long volume = (long) docs.size();
            volume += crawlDo.getVolume();
            // updating crawl status
            Long end = System.currentTimeMillis();
            crawlDao.updateCrawlFinish(crawlId, STOP_STATUS, (end - begin) + duration, new Long(volume),
                    new Date(end));
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(CrawlService.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(CrawlService.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    /**
     * Parse the bytes as a CSV file with utf-8 encoding.
     * @param bytes
     * @param crawlId
     * @return
     * @throws FileNotFoundException
     * @throws IOException 
     */
    private List<SemdeeDocument> parseCSV(byte[] bytes, Long crawlId) throws FileNotFoundException, IOException {
        List<SemdeeDocument> docs = new ArrayList<SemdeeDocument>();
        int nbrDocument = 0;
        InputStream is = new ByteArrayInputStream(bytes);
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        String lect = br.readLine();
        try {
            while (lect != null) {
                nbrDocument++;
                Date date = new Date(System.currentTimeMillis());
                String[] line = lect.split(";");
                SemdeeDocument doc = new SemdeeDocument();
                doc.setCrawlId(crawlId);
                doc.setSource("3");
                doc.setUrl(line[0]);
                doc.setTitle(line[1]);
                doc.setDescription(line[2]);
                doc.setShortDescription(line[2].substring(0, Math.min(line[2].length(), 250)));
                doc.setTags(line[3]);
                doc.seteDate(date);
                doc.setAuthor(line[4]);
                doc.setLanguage(line[5]);
                docs.add(doc);
                lect = br.readLine();
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.err.println("ArrayIndexOutOfBoundsException at" +nbrDocument +" for "+ lect);
            throw new ArrayIndexOutOfBoundsException();
        }
        br.close();
        return docs;
    }

    /**
     * Upload a csv file with settings.
     *
     * @param csv Csv file with settings.
     * @param crawlId
     * @param userId
     * @return
     */
    @Transactional
    public boolean uploadFileAdvanced(CsvAdvancedDO csv, Long crawlId, Long userId) {
        LOGGER.info("{CrawlService::uploadFileAdvanced} crawlId:{} - userId:{} "
                + "- fileLength:{}", crawlId, userId, csv.getBytes().length);
        try {
            Long begin = System.currentTimeMillis();
            Crawl crawlDo = crawlDao.getCrawl(crawlId, userId);
            if (crawlDo == null) {
                LOGGER.info("crawlId={} -> specified crawl does not exists or is attached to another user", crawlId);
                return false;
            }

            Long duration = crawlDo.getDuration();
            List<SemdeeDocument> docs = new ArrayList<SemdeeDocument>();
            docs = parseCSVAdvanced(csv, crawlId);
            Source source = new Source();
            source.setId(3L);
            crawlDao.writeDocumentsInBdd(crawlId, docs, source);
            Long volume = (long) docs.size();
            volume += crawlDo.getVolume();
            // updating crawl status
            Long end = System.currentTimeMillis();
            crawlDao.updateCrawlFinish(crawlId, STOP_STATUS, (end - begin) + duration, new Long(volume),
                    new Date(end));
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(CrawlService.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(CrawlService.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    /**
     * Parse a csv file according to the settings.
     * @param csv
     * @param crawlId
     * @return
     * @throws FileNotFoundException
     * @throws IOException 
     */
    private List<SemdeeDocument> parseCSVAdvanced(CsvAdvancedDO csv, Long crawlId) 
            throws FileNotFoundException, IOException {
        List<SemdeeDocument> docs = new ArrayList<SemdeeDocument>();

        InputStream is = null;
        InputStreamReader reader = null;
        
        try {
            is = new ByteArrayInputStream(csv.getBytes());
            reader = new InputStreamReader(is, csv.getSettings().getEncoding());
            
            boolean isHeader = csv.getSettings().isIsheader();
            char delimiter = csv.getSettings().getCsvDelimiter().charAt(0);
            char quote = csv.getSettings().getStringDelimiter().charAt(0);
            CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT
                    .withDelimiter(delimiter)
                    .withQuote(quote)
                    );
            for (CSVRecord r : parser){
                if (isHeader){
                    isHeader = false;
                    continue;
                }
                String url = "";
                String title = "";
                String description = "";
                String tags = "";
                String author = "";
                String language = "";
                
                int i0 = csv.getSettings().getMap("ID");
                int i1 = csv.getSettings().getMap("Title");
                int i2 = csv.getSettings().getMap("Description");
                int i3 = csv.getSettings().getMap("Tags");
                int i4 = csv.getSettings().getMap("Author");
                int i5 = csv.getSettings().getMap("Language");
                if (i0 > -1)
                    url = r.get(i0);
                if (i1 > -1)
                    title = r.get(i1);
                if (i2 > -1)
                    description = r.get(i2);
                if (i3 > -1)
                    tags = r.get(i3);
                if (i4 > -1)
                    author = r.get(i4);
                if (i5 > -1)
                    language = r.get(i5);
                
                Date date = new Date(System.currentTimeMillis());
                SemdeeDocument doc = new SemdeeDocument();
                doc.setCrawlId(crawlId);
                doc.setSource("3");
                doc.setUrl(url);
                doc.setTitle(title);
                doc.setDescription(description);
                doc.setShortDescription(description.substring(0, Math.min(description.length(), 250)));
                doc.setTags(tags);
                doc.seteDate(date);
                doc.setAuthor(author);
                doc.setLanguage(language);
                docs.add(doc);
                
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
        }
        return docs;
    }

} // fin de CrawlService

