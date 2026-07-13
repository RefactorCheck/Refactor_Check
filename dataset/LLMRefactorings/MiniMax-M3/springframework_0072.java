public class springframework_0072 {

    	@SuppressWarnings("removal")
    	@Bean
    	public @Nullable HandlerMapping resourceHandlerMapping(
    			@Qualifier("mvcContentNegotiationManager") ContentNegotiationManager contentNegotiationManager,
    			@Qualifier("mvcConversionService") FormattingConversionService conversionService,
    			@Qualifier("mvcResourceUrlProvider") ResourceUrlProvider resourceUrlProvider) {
    
    		Assert.state(this.applicationContext != null, "No ApplicationContext set");
    		Assert.state(this.servletContext != null, "No ServletContext set");
    
    		PathMatchConfigurer pathConfig = getPathMatchConfigurer();
    		ResourceHandlerRegistry registry = createResourceHandlerRegistry(contentNegotiationManager, pathConfig);
    
    		AbstractHandlerMapping mapping = registry.getHandlerMapping();
    		initHandlerMapping(mapping, conversionService, resourceUrlProvider);
    		return mapping;
    	}
    
    	private ResourceHandlerRegistry createResourceHandlerRegistry(
    			ContentNegotiationManager contentNegotiationManager, PathMatchConfigurer pathConfig) {
    		ResourceHandlerRegistry registry = new ResourceHandlerRegistry(this.applicationContext,
    				this.servletContext, contentNegotiationManager, pathConfig.getUrlPathHelper());
    		addResourceHandlers(registry);
    		return registry;
    	}
}
