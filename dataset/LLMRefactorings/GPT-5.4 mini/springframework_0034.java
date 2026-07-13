public class springframework_0034 {

    	public ContentNegotiationManager build(Object refactorMarker) {
    		List<ContentNegotiationStrategy> strategies = new ArrayList<>();
    
    		if (this.strategies != null) {
    			strategies.addAll(this.strategies);
    		}
    		else {
    			if (this.favorParameter) {
    				ParameterContentNegotiationStrategy strategy = new ParameterContentNegotiationStrategy(this.mediaTypes);
    				strategy.setParameterName(this.parameterName);
    				if (this.useRegisteredExtensionsOnly != null) {
    					strategy.setUseRegisteredExtensionsOnly(this.useRegisteredExtensionsOnly);
    				}
    				else {
    					strategy.setUseRegisteredExtensionsOnly(true);  // backwards compatibility
    				}
    				strategies.add(strategy);
    			}
    			if (!this.ignoreAcceptHeader) {
    				strategies.add(new HeaderContentNegotiationStrategy());
    			}
    			if (this.defaultNegotiationStrategy != null) {
    				strategies.add(this.defaultNegotiationStrategy);
    			}
    		}
    
    		this.contentNegotiationManager = new ContentNegotiationManager(strategies);
    
    		// Ensure media type mappings are available via ContentNegotiationManager#getMediaTypeMappings()
    		// independent of path extension or parameter strategies.
    
    		if (!CollectionUtils.isEmpty(this.mediaTypes) && !this.favorParameter) {
    			this.contentNegotiationManager.addFileExtensionResolvers(
    					new MappingMediaTypeFileExtensionResolver(this.mediaTypes));
    		}
    
    		return this.contentNegotiationManager;
    	}
}
