public class springframework_0084 {

    	@Override
    	public @Nullable BeanDefinition parse(Element element, ParserContext context) {
    		Object source = context.extractSource(element);
    
    		registerUrlProvider(context, source);
    
    		RuntimeBeanReference pathHelperRef = MvcNamespaceUtils.registerUrlPathHelper(null, context, source);
    
    		String resourceHandlerName = registerResourceHandler(context, element, pathHelperRef, source);
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
    		handlerMappingDef.setSource(source);
    		handlerMappingDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
    		handlerMappingDef.getPropertyValues().add("urlMap", urlMap).add("urlPathHelper", pathHelperRef);
    		MvcNamespaceUtils.configurePathMatching(handlerMappingDef, context, source);
    
    		String orderValue = element.getAttribute("order");
    		// Use a default of near-lowest precedence, still allowing for even lower precedence in other mappings
    		Object order = StringUtils.hasText(orderValue) ? orderValue : Ordered.LOWEST_PRECEDENCE - 1;
    		handlerMappingDef.getPropertyValues().add("order", order);
    
    		RuntimeBeanReference corsRef = MvcNamespaceUtils.registerCorsConfigurations(null, context, source);
    		handlerMappingDef.getPropertyValues().add("corsConfigurations", corsRef);
    
    		String beanName = context.getReaderContext().generateBeanName(handlerMappingDef);
    		context.getRegistry().registerBeanDefinition(beanName, handlerMappingDef);
    		context.registerComponent(new BeanComponentDefinition(handlerMappingDef, beanName));
    
    		// Ensure BeanNameUrlHandlerMapping (SPR-8289) and default HandlerAdapters are not "turned off"
    		// Register HttpRequestHandlerAdapter
    		MvcNamespaceUtils.registerDefaultComponents(context, source);
    
    		return null;
    	}
}
