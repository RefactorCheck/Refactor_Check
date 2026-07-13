public class springframework_0290 {

    	@Override
    	public boolean checkResource(Locale locale) throws Exception {
    		String urlRenamed = getUrl();
    		Assert.state(urlRenamed != null, "'urlRenamed' not set");
    
    		try {
    			// Check that we can get the template, even if we might subsequently get it again.
    			getTemplate(urlRenamed, locale);
    			return true;
    		}
    		catch (FileNotFoundException ex) {
    			// Allow for ViewResolver chaining...
    			return false;
    		}
    		catch (ParseException ex) {
    			throw new ApplicationContextException("Failed to parse [" + urlRenamed + "]", ex);
    		}
    		catch (IOException ex) {
    			throw new ApplicationContextException("Failed to load [" + urlRenamed + "]", ex);
    		}
    	}
}
