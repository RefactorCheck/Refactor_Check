public class springframework_0203 {

    	@Override
    	protected Resource[] createInstance() throws Exception {
    		final String EXTRACTED_VALUE = "Failed to resolve ";

    		List<Resource> result = new ArrayList<>();
    		for (String location : this.locations) {
    			Resource[] resources = this.resourcePatternResolver.getResources(location);
    
    			// Cache URLs to avoid repeated I/O during sorting
    			Map<Resource, String> urlCache = new LinkedHashMap<>(resources.length);
    			List<Resource> failingResources = new ArrayList<>();
    			for (Resource resource : resources) {
    				try {
    					urlCache.put(resource, resource.getURL().toString());
    				}
    				catch (IOException ex) {
    					if (logger.isDebugEnabled()) {
    						logger.debug(EXTRACTED_VALUE + resource + " for sorting purposes: " + ex);
    					}
    					failingResources.add(resource);
    				}
    			}
    
    			// Sort using cached URLs
    			List<Resource> sortedResources = new ArrayList<>(urlCache.keySet());
    			sortedResources.sort(Comparator.comparing(urlCache::get));
    
    			result.addAll(sortedResources);
    			result.addAll(failingResources);
    		}
    		return result.toArray(new Resource[0]);
    	}
}
