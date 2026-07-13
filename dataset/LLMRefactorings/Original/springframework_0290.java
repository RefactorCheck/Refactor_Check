public class springframework_0290 {

    	@Override
    	public boolean checkResource(Locale locale) throws Exception {
    		String url = getUrl();
    		Assert.state(url != null, "'url' not set");
    
    		try {
    			// Check that we can get the template, even if we might subsequently get it again.
    			getTemplate(url, locale);
    			return true;
    		}
    		catch (FileNotFoundException ex) {
    			// Allow for ViewResolver chaining...
    			return false;
    		}
    		catch (ParseException ex) {
    			throw new ApplicationContextException("Failed to parse [" + url + "]", ex);
    		}
    		catch (IOException ex) {
    			throw new ApplicationContextException("Failed to load [" + url + "]", ex);
    		}
    	}
}
