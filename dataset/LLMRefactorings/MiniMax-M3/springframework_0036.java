public class springframework_0036 {

    	@Override
    	@SuppressWarnings("NullAway") // Dataflow analysis limitation
    	public final @Nullable BeanDefinition parse(Element element, ParserContext parserContext) {
    		AbstractBeanDefinition definition = parseInternal(element, parserContext);
    		if (definition != null && !parserContext.isNested()) {
    			try {
    				String id = resolveId(element, definition, parserContext);
    				if (!StringUtils.hasText(id)) {
    					parserContext.getReaderContext().error(
    							"Id is required for element '" + parserContext.getDelegate().getLocalName(element) +
    							"' when used as a top-level tag", element);
    				}
    				String[] aliases = parseAliases(element);
    				BeanDefinitionHolder holder = new BeanDefinitionHolder(definition, id, aliases);
    				registerBeanDefinition(holder, parserContext.getRegistry());
    				if (shouldFireEvents()) {
    					BeanComponentDefinition componentDefinition = new BeanComponentDefinition(holder);
    					postProcessComponentDefinition(componentDefinition);
    					parserContext.registerComponent(componentDefinition);
    				}
    			}
    			catch (BeanDefinitionStoreException ex) {
    				String msg = ex.getMessage();
    				parserContext.getReaderContext().error((msg != null ? msg : ex.toString()), element);
    				return null;
    			}
    		}
    		return definition;
    	}

    	private String[] parseAliases(Element element) {
    		if (!shouldParseNameAsAliases()) {
    			return null;
    		}
    		String name = element.getAttribute(NAME_ATTRIBUTE);
    		if (!StringUtils.hasLength(name)) {
    			return null;
    		}
    		return StringUtils.trimArrayElements(StringUtils.commaDelimitedListToStringArray(name));
    	}
}
