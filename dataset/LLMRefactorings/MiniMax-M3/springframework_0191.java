public class springframework_0191 {

	private Map<String, String> getSchemaMappings() {
		Map<String, String> schemaMappings = this.schemaMappings;
		if (schemaMappings == null) {
			synchronized (this) {
				schemaMappings = this.schemaMappings;
				if (schemaMappings == null) {
					schemaMappings = loadSchemaMappings();
					this.schemaMappings = schemaMappings;
				}
			}
		}
		return schemaMappings;
	}

	private Map<String, String> loadSchemaMappings() {
		if (logger.isTraceEnabled()) {
			logger.trace("Loading schema mappings from [" + this.schemaMappingsLocation + "]");
		}
		try {
			Properties mappings =
					PropertiesLoaderUtils.loadAllProperties(this.schemaMappingsLocation, this.classLoader);
			if (logger.isTraceEnabled()) {
				logger.trace("Loaded schema mappings: " + mappings);
			}
			Map<String, String> schemaMappings = new ConcurrentHashMap<>(mappings.size());
			CollectionUtils.mergePropertiesIntoMap(mappings, schemaMappings);
			return schemaMappings;
		}
		catch (IOException ex) {
			throw new IllegalStateException(
					"Unable to load schema mappings from location [" + this.schemaMappingsLocation + "]", ex);
		}
	}
}
