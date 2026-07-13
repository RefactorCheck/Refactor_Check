public class springframework_0138 {

    	@Override
    	public @Nullable BeanDefinition parse(Element element, ParserContext parserContext) {
    
    		Map<String, CorsConfiguration> corsConfigurations = new LinkedHashMap<>();
    		List<Element> mappings = DomUtils.getChildElementsByTagName(element, "mapping");
    
    		if (mappings.isEmpty()) {
    			CorsConfiguration config = new CorsConfiguration().applyPermitDefaultValues();
    			corsConfigurations.put("/**", config);
    		}
    		else {
    			for (Element mapping : mappings) {
    				CorsConfiguration config = new CorsConfiguration();
    				applyStringListAttribute(mapping, "allowed-origins", config::setAllowedOrigins);
    				applyStringListAttribute(mapping, "allowed-origin-patterns", config::setAllowedOriginPatterns);
    				applyStringListAttribute(mapping, "allowed-methods", config::setAllowedMethods);
    				applyStringListAttribute(mapping, "allowed-headers", config::setAllowedHeaders);
    				applyStringListAttribute(mapping, "exposed-headers", config::setExposedHeaders);
    				if (mapping.hasAttribute("allow-credentials")) {
    					config.setAllowCredentials(Boolean.parseBoolean(mapping.getAttribute("allow-credentials")));
    				}
    				if (mapping.hasAttribute("max-age")) {
    					config.setMaxAge(Long.parseLong(mapping.getAttribute("max-age")));
    				}
    				config.applyPermitDefaultValues();
    				config.validateAllowCredentials();
    				config.validateAllowPrivateNetwork();
    				corsConfigurations.put(mapping.getAttribute("path"), config);
    			}
    		}
    
    		MvcNamespaceUtils.registerCorsConfigurations(
    				corsConfigurations, parserContext, parserContext.extractSource(element));
    		return null;
    	}

    	private static void applyStringListAttribute(Element mapping, String attributeName, Consumer<List<String>> setter) {
    		if (mapping.hasAttribute(attributeName)) {
    			String[] values = StringUtils.tokenizeToStringArray(mapping.getAttribute(attributeName), ",");
    			setter.accept(Arrays.asList(values));
    		}
    	}
}
