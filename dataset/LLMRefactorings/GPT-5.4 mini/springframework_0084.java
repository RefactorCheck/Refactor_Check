public class springframework_0084 {

    	@Override
    	public @Nullable BeanDefinition parse(Element element, ParserContext context) {
    
    		registerUrlProvider(context, (context.extractSource(element)));
    
    		RuntimeBeanReference pathHelperRef = MvcNamespaceUtils.registerUrlPathHelper(null, context, (context.extractSource(element)));
    
    		String resourceHandlerName = registerResourceHandler(context, element, pathHelperRef, (context.extractSource(element)));
    		if (resourceHandlerName == null) {
    			return null;
    		}
    
    		Map<String, String> urlMap = new ManagedMap<>();
    		String resourceRequestPath = element.getAttribute("mapping");
    		if (!StringUtils.hasText(resourceRequestPath)) {
    			context.getReaderContext().error("The 'mapping' attribute is required.", context.extractSource(element));
    			return null;
    		}
    		urlMap.put(resourceRequestPath, resourceHandlerName);
    
    		RootBeanDefinition handlerMappingDef = new RootBeanDefinition(SimpleUrlHandlerMapping.class);
    		handlerMappingDef.setSource((context.extractSource(element)));
    		handlerMappingDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
    		handlerMappingDef.getPropertyValues().add("urlMap", urlMap).add("urlPathHelper", pathHelperRef);
    		MvcNamespaceUtils.configurePathMatching(handlerMappingDef, context, (context.extractSource(element)));
    
    		String orderValue = element.getAttribute("order");
    		// Use a default of near-lowest precedence, still allowing for even lower precedence in other mappings
    		Object order = StringUtils.hasText(orderValue) ? orderValue : Ordered.LOWEST_PRECEDENCE - 1;
    		handlerMappingDef.getPropertyValues().add("order", order);
    
    		RuntimeBeanReference corsRef = MvcNamespaceUtils.registerCorsConfigurations(null, context, (context.extractSource(element)));
    		handlerMappingDef.getPropertyValues().add("corsConfigurations", corsRef);
    
    		String beanName = context.getReaderContext().generateBeanName(handlerMappingDef);
    		context.getRegistry().registerBeanDefinition(beanName, handlerMappingDef);
    		context.registerComponent(new BeanComponentDefinition(handlerMappingDef, beanName));
    
    		// Ensure BeanNameUrlHandlerMapping (SPR-8289) and default HandlerAdapters are not "turned off"
    		// Register HttpRequestHandlerAdapter
    		MvcNamespaceUtils.registerDefaultComponents(context, (context.extractSource(element)));
    
    		return null;
    	}
}
