public class springframework_0149 {

    	@Override
    	protected MutablePropertyValues parseCommonContainerProperties(Element containerEle, ParserContext parserContext) {
    		applyExtractedRefactoring();

    		Integer acknowledgeMode = parseAcknowledgeMode(containerEle, parserContext);
    		if (acknowledgeMode != null) {
    			properties.add("acknowledgeMode", acknowledgeMode);
    		}
    
    		String concurrency = containerEle.getAttribute(CONCURRENCY_ATTRIBUTE);
    		if (StringUtils.hasText(concurrency)) {
    			properties.add("concurrency", concurrency);
    		}
    
    		String prefetch = containerEle.getAttribute(PREFETCH_ATTRIBUTE);
    		if (StringUtils.hasText(prefetch)) {
    			properties.add("prefetchSize", Integer.valueOf(prefetch));
    		}
    
    		return properties;
    	}

	private void applyExtractedRefactoring() {
    		MutablePropertyValues properties = super.parseCommonContainerProperties(containerEle, parserContext);
	}
}
