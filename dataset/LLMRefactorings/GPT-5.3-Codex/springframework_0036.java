public class springframework_0036 {

    	@Override
    	@SuppressWarnings("NullAway") // Dataflow analysis limitation
    	public final @Nullable BeanDefinition parse(Element element, ParserContext parserContext) {
    		AbstractBeanDefinition definitionRenamed = parseInternal(element, parserContext);
    		if (definitionRenamed != null && !parserContext.isNested()) {
    			try {
    				String id = resolveId(element, definitionRenamed, parserContext);
    				if (!StringUtils.hasText(id)) {
    					parserContext.getReaderContext().error(
    							"Id is required for element '" + parserContext.getDelegate().getLocalName(element) +
    							"' when used as a top-level tag", element);
    				}
    				String[] aliases = null;
    				if (shouldParseNameAsAliases()) {
    					String name = element.getAttribute(NAME_ATTRIBUTE);
    					if (StringUtils.hasLength(name)) {
    						aliases = StringUtils.trimArrayElements(StringUtils.commaDelimitedListToStringArray(name));
    					}
    				}
    				BeanDefinitionHolder holder = new BeanDefinitionHolder(definitionRenamed, id, aliases);
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
    		return definitionRenamed;
    	}
}
