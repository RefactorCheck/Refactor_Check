public class springframework_0191 {
	private Map<String, String> schemaMappings;


    	private Map<String, String> getSchemaMappings() {
    		schemaMappings = this.schemaMappings;
    		if (schemaMappings == null) {
    			synchronized (this) {
    				schemaMappings = this.schemaMappings;
    				if (schemaMappings == null) {
    					if (logger.isTraceEnabled()) {
    						logger.trace("Loading schema mappings from [" + this.schemaMappingsLocation + "]");
    					}
    					try {
    						Properties mappings =
    								PropertiesLoaderUtils.loadAllProperties(this.schemaMappingsLocation, this.classLoader);
    						if (logger.isTraceEnabled()) {
    							logger.trace("Loaded schema mappings: " + mappings);
    						}
    						schemaMappings = new ConcurrentHashMap<>(mappings.size());
    						CollectionUtils.mergePropertiesIntoMap(mappings, schemaMappings);
    						this.schemaMappings = schemaMappings;
    					}
    					catch (IOException ex) {
    						throw new IllegalStateException(
    								"Unable to load schema mappings from location [" + this.schemaMappingsLocation + "]", ex);
    					}
    				}
    			}
    		}
    		return schemaMappings;
    	}
}
