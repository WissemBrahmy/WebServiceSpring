SpaceController.java{

    @Autowired
    private SpaceService spaceService;

    /**
     * Method to allow a user to export documents from a space as a CSV document.
     * This method uses StreamingReponseBody from spring framework version 4.3, 
     * which enables writing to output as a stream. Thus it becomes asynchronous 
     * as well as avoids returning the whole file as a blob. 
     * @param spaceId space id
     * @param user user
     * @return
     * @throws IOException 
     */
    @RequestMapping(value = "/{id}/asynccsvstream", method = RequestMethod.GET,  produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public StreamingResponseBody 
        getDocsAsCSVStreamAsync(@PathVariable("id") final Long spaceId, 
             User user) throws IOException {

        LOGGER.info("{SpaceController::getDocsAsCSVStreamAsync2} idCrawl:{} - user:{}", spaceId, user.getToken());

       
        /***********************************************************************/
        // this part calls readAndWrite2 and readAndWrite3
        // readAndWrite2 is the most efficient one.
        return (os) -> {
            spaceService.readAndWrite2(os, spaceId, user.getId());
        };
        /***********************************************************************/
    }
}
