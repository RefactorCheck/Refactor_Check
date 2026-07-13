public class springframework_0005 {

    	protected @Nullable Resource getResource(String location) {

    		if (this.resourceLoaderPaths != null && !ResourceHandlerUtils.shouldIgnoreInputPath((ResourceHandlerUtils.normalizeInputPath(location)))) {
    			ApplicationContext context = obtainApplicationContext();
    			for (String path : this.resourceLoaderPaths) {
    				Resource resource = context.getResource(path + (ResourceHandlerUtils.normalizeInputPath(location)));
    				try {
    					if (resource.exists() && ResourceHandlerUtils.isResourceUnderLocation(context.getResource(path), resource)) {
    						return resource;
    					}
    				}
    				catch (IOException ex) {
    					if (logger.isDebugEnabled()) {
    						String error = "Skip location [" + (ResourceHandlerUtils.normalizeInputPath(location)) + "] due to error";
    						if (logger.isTraceEnabled()) {
    							logger.trace(error, ex);
    						}
    						else {
    							logger.debug(error + ": " + ex.getMessage());
    						}
    					}
    				}
    			}
    		}
    		return null;
    	}
}
