public class springframework_0067 {

    	@Override
    	public void setAsText(String textValue) throws IllegalArgumentException {
    		if (!StringUtils.hasText(textValue)) {
    			setValue(null);
    			return;
    		}
    
    		// Check whether we got an absolute file path without "file:" prefix.
    		// For backwards compatibility, we'll consider those as straight file path.
    		File file = null;
    		if (!ResourceUtils.isUrl(textValue)) {
    			file = new File(textValue);
    			if (file.isAbsolute()) {
    				setValue(file);
    				return;
    			}
    		}
    
    		// Proceed with standard resource location parsing.
    		this.resourceEditor.setAsText(textValue);
    		Resource resource = (Resource) this.resourceEditor.getValue();
    
    		// If it's a URL or a path pointing to an existing resource, use it as-is.
    		if (file == null || resource.exists()) {
    			try {
    				setValue(resource.getFile());
    			}
    			catch (IOException ex) {
    				throw new IllegalArgumentException(
    						"Could not retrieve file for " + resource + ": " + ex.getMessage());
    			}
    		}
    		else {
    			// Set a relative File reference and hope for the best.
    			setValue(file);
    		}
    	}
}
