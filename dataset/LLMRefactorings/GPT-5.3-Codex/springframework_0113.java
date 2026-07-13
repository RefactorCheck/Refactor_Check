public class springframework_0113 {

    	public Resource findLocalizedResource(String name, String extension, @Nullable Locale locale) {
    		applyExtractedRefactoring();

    		Assert.notNull(extension, "Extension must not be null");
    
    		Resource resource = null;
    
    		if (locale != null) {
    			String lang = locale.getLanguage();
    			String country = locale.getCountry();
    			String variant = locale.getVariant();
    
    			// Check for file with language, country and variant localization.
    			if (!variant.isEmpty()) {
    				String location =
    						name + this.separator + lang + this.separator + country + this.separator + variant + extension;
    				resource = this.resourceLoader.getResource(location);
    			}
    
    			// Check for file with language and country localization.
    			if ((resource == null || !resource.exists()) && !country.isEmpty()) {
    				String location = name + this.separator + lang + this.separator + country + extension;
    				resource = this.resourceLoader.getResource(location);
    			}
    
    			// Check for document with language localization.
    			if ((resource == null || !resource.exists()) && !lang.isEmpty()) {
    				String location = name + this.separator + lang + extension;
    				resource = this.resourceLoader.getResource(location);
    			}
    		}
    
    		// Check for document without localization.
    		if (resource == null || !resource.exists()) {
    			String location = name + extension;
    			resource = this.resourceLoader.getResource(location);
    		}
    
    		return resource;
    	}

	private void applyExtractedRefactoring() {
    		Assert.notNull(name, "Name must not be null");
	}
}
