public class springframework_0206 {

    	@Bean
    	public RequestMappingHandlerMapping requestMappingHandlerMapping(
    			@Qualifier("webFluxContentTypeResolver") RequestedContentTypeResolver contentTypeResolver,
    			@Qualifier("webFluxApiVersionStrategy") @Nullable ApiVersionStrategy apiVersionStrategy) {
    
    		RequestMappingHandlerMapping mapping = createRequestMappingHandlerMapping();
    		mapping.setOrder(0);
    		mapping.setContentTypeResolver(contentTypeResolver);
    		mapping.setApiVersionStrategy(apiVersionStrategy);
    
    		PathMatchConfigurer configurer = getPathMatchConfigurer();
    		configureAbstractHandlerMapping(mapping, configurer);
    
    		Map<String, Predicate<Class<?>>> pathPrefixes = configurer.getPathPrefixes();
    		if (pathPrefixes != null) {
    			mapping.setPathPrefixes(pathPrefixes);
    		}
    
    		return mapping;
    	}
}
