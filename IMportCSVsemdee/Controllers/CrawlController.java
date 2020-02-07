@RestController
@RequestMapping("/crawls")
public class CrawlController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CrawlController.class);

    @Autowired
    private CrawlService crawlService;

    …………..
    
      /**
     * Import documents via CSV.
     * @param bytes
     * @param crawlId
     * @param user
     * @return 
     */
    @RequestMapping(value = "/upload/{id}", method = RequestMethod.POST)
    public Response uploadFile(@RequestBody byte[] bytes, @PathVariable("id") final long crawlId, User user) {
        LOGGER.info("{CrawlController::uploadFile} => crawlId={} - userId:{} "
                + "- fileLength:{}", crawlId, user.getId(),bytes.length);
        Response<Void> response = new Response<Void>();
        boolean result = crawlService.uploadFile(bytes, crawlId, user.getId());
        response.setStatus(result ? 200 : 500);
        response.setData(null);
        return response;
    }

 /**
     * Import documents via CSV with settings. 
     * @param bytes
     * @param crawlId
     * @param user
     * @return 
     */
    @RequestMapping(value = "/uploadadvanced/{id}", method = RequestMethod.POST
    , consumes=MediaType.APPLICATION_JSON_VALUE)
    public Response uploadFileAdvanced(@RequestBody CsvAdvancedDO csv, 
            @PathVariable("id") final long crawlId, 
            User user) {
        LOGGER.info("{CrawlController::uploadFile} => crawlId={} - userId:{} "
                + "- fileLength:{}", crawlId, user.getId(),csv.getBytes().length);
        Response<Void> response = new Response<Void>();
        boolean result = crawlService.uploadFileAdvanced(csv, crawlId, user.getId());
        response.setStatus(result ? 200 : 500);
        response.setData(null);
        return response;
    }

}
