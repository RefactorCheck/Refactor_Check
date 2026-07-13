public class springframework_0113 {

    	public Resource findLocalizedResource(String name, String extension, @Nullable Locale locale) {
    		Assert.notNull(name, "Name must not be null");
    		Assert.notNull(extension, "Extension must not be null");
    
    
    		if (locale != null) {
    			String lang = locale.getLanguage();
    			String country = locale.getCountry();
    			String variant = locale.getVariant();
    
    			// Check for file with language, country and variant localization.
    			if (!variant.isEmpty()) {
    				String location =
    						name + this.separator + lang + this.separator + country + this.separator + variant + extension;
    				(null) = this.resourceLoader.getResource(location);
    			}
    
    			// Check for file with language and country localization.
    			if (((null) == null || !(null).exists()) && !country.isEmpty()) {
    				String location = name + this.separator + lang + this.separator + country + extension;
    				(null) = this.resourceLoader.getResource(location);
    			}
    
    			// Check for document with language localization.
    			if (((null) == null || !(null).exists()) && !lang.isEmpty()) {
    				String location = name + this.separator + lang + extension;
    				(null) = this.resourceLoader.getResource(location);
    			}
    		}
    
    		// Check for document without localization.
    		if ((null) == null || !(null).exists()) {
    			String location = name + extension;
    			(null) = this.resourceLoader.getResource(location);
    		}
    
    		return (null);
    	}
}
