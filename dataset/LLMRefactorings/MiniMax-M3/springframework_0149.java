public class springframework_0149 {

    	@Override
    	protected MutablePropertyValues parseCommonContainerProperties(Element containerEle, ParserContext parserContext) {
    		MutablePropertyValues properties = super.parseCommonContainerProperties(containerEle, parserContext);
    
    		addAcknowledgeModeProperty(properties, containerEle, parserContext);
    		addConcurrencyProperty(properties, containerEle);
    		addPrefetchProperty(properties, containerEle);
    
    		return properties;
    	}
    
    	private void addAcknowledgeModeProperty(MutablePropertyValues properties, Element containerEle, ParserContext parserContext) {
    		Integer acknowledgeMode = parseAcknowledgeMode(containerEle, parserContext);
    		if (acknowledgeMode != null) {
    			properties.add("acknowledgeMode", acknowledgeMode);
    		}
    	}
    
    	private void addConcurrencyProperty(MutablePropertyValues properties, Element containerEle) {
    		String concurrency = containerEle.getAttribute(CONCURRENCY_ATTRIBUTE);
    		if (StringUtils.hasText(concurrency)) {
    			properties.add("concurrency", concurrency);
    		}
    	}
    
    	private void addPrefetchProperty(MutablePropertyValues properties, Element containerEle) {
    		String prefetch = containerEle.getAttribute(PREFETCH_ATTRIBUTE);
    		if (StringUtils.hasText(prefetch)) {
    			properties.add("prefetchSize", Integer.valueOf(prefetch));
    		}
    	}
}
