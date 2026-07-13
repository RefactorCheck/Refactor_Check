public class springframework_0096 {

    	protected Mono<Resource> getResource(String resourcePath, Resource location) {
    		try {

    			if ((ResourceHandlerUtils.createRelativeResource(location, resourcePath)).isReadable()) {
    				if (checkResource((ResourceHandlerUtils.createRelativeResource(location, resourcePath)), location)) {
    					return Mono.just((ResourceHandlerUtils.createRelativeResource(location, resourcePath)));
    				}
    				else if (logger.isWarnEnabled()) {
    					Resource[] allowed = getAllowedLocations();
    					logger.warn(LogFormatUtils.formatValue(
    							"Resource path \"" + resourcePath + "\" was successfully resolved " +
    									"but (ResourceHandlerUtils.createRelativeResource(location, resourcePath)) \"" + (ResourceHandlerUtils.createRelativeResource(location, resourcePath)) + "\" is neither under the " +
    									"current location \"" + location + "\" nor under any of the " +
    									"allowed locations " + (allowed != null ? Arrays.asList(allowed) : "[]"), -1, true));
    				}
    			}
    			return Mono.empty();
    		}
    		catch (IOException ex) {
    			if (logger.isDebugEnabled()) {
    				String error = "Skip location [" + location + "] due to error";
    				if (logger.isTraceEnabled()) {
    					logger.trace(error, ex);
    				}
    				else {
    					logger.debug(error + ": " + ex.getMessage());
    				}
    			}
    			return Mono.error(ex);
    		}
    	}
}
