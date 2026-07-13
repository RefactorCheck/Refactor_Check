public class springframework_0290 {

    private static final String URL_NOT_SET_MESSAGE = "'url' not set";
    private static final String FAILED_TO_PARSE_PREFIX = "Failed to parse [";
    private static final String FAILED_TO_LOAD_PREFIX = "Failed to load [";

    @Override
    public boolean checkResource(Locale locale) throws Exception {
        String url = getUrl();
        Assert.state(url != null, URL_NOT_SET_MESSAGE);

        try {
            getTemplate(url, locale);
            return true;
        }
        catch (FileNotFoundException ex) {
            return false;
        }
        catch (ParseException ex) {
            throw new ApplicationContextException(FAILED_TO_PARSE_PREFIX + url + "]", ex);
        }
        catch (IOException ex) {
            throw new ApplicationContextException(FAILED_TO_LOAD_PREFIX + url + "]", ex);
        }
    }
}
