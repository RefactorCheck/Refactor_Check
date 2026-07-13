public class springframework_0065 {

    	@Override
    	protected @Nullable String resolveUrlPathInternal(String resourceUrlPath,
    			List<? extends Resource> locations, ResourceResolverChain chain) {
    
    		String key = RESOLVED_URL_PATH_CACHE_KEY_PREFIX + resourceUrlPath;
    		String resolvedUrlPath = this.cache.get(key, String.class);
    
    		if (resolvedUrlPath != null) {
    			if (logger.isTraceEnabled()) {
    				logger.trace("Path resolved from cache");
    			}
    			return resolvedUrlPath;
    		}
    
    		resolvedUrlPath = chain.resolveUrlPath(resourceUrlPath, locations);
    		if (resolvedUrlPath != null) {
    			this.cache.put(key, resolvedUrlPath);
    		}
    
    		return resolvedUrlPath;
    	}
}
