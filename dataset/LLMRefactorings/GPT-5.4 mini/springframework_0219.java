public class springframework_0219 {

    	@Override
    	@SuppressWarnings("unchecked")
    	public @Nullable BeanDefinition parse(Element element, ParserContext parserContext) {
    		return springframework_0219Extracted("unchecked");
    	}

    	@SuppressWarnings("unchecked")
    	private @Nullable BeanDefinition parse(Element element, ParserContext parserContext) {
    		Object source = parserContext.extractSource(element);
    
    		// Register SimpleUrlHandlerMapping for view controllers
    		BeanDefinition hm = registerHandlerMapping(parserContext, source);
    
    		// Ensure BeanNameUrlHandlerMapping (SPR-8289) and default HandlerAdapters are not "turned off"
    		MvcNamespaceUtils.registerDefaultComponents(parserContext, source);
    
    		// Create view controller bean definition
    		RootBeanDefinition controller = new RootBeanDefinition(ParameterizableViewController.class);
    		controller.setSource(source);
    
    		HttpStatusCode statusCode = null;
    		if (element.hasAttribute("status-code")) {
    			int statusValue = Integer.parseInt(element.getAttribute("status-code"));
    			statusCode = HttpStatusCode.valueOf(statusValue);
    		}
    
    		String name = element.getLocalName();
    		switch (name) {
    			case "view-controller" -> {
    				if (element.hasAttribute("view-name")) {
    					controller.getPropertyValues().add("viewName", element.getAttribute("view-name"));
    				}
    				if (statusCode != null) {
    					controller.getPropertyValues().add("statusCode", statusCode);
    				}
    			}
    			case "redirect-view-controller" ->
    				controller.getPropertyValues().add("view", getRedirectView(element, statusCode, source));
    			case "status-controller" -> {
    				controller.getPropertyValues().add("statusCode", statusCode);
    				controller.getPropertyValues().add("statusOnly", true);
    			}
    			default ->
    				// Should never happen...
    				throw new IllegalStateException("Unexpected tag name: " + name);
    		}
    
    		Map<String, BeanDefinition> urlMap = (Map<String, BeanDefinition>) hm.getPropertyValues().get("urlMap");
    		if (urlMap == null) {
    			urlMap = new ManagedMap<>();
    			hm.getPropertyValues().add("urlMap", urlMap);
    		}
    		urlMap.put(element.getAttribute("path"), controller);
    
    		return null;
    	}
}
