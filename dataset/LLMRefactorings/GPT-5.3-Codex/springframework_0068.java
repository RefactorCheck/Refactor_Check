public class springframework_0068 {

    	protected void parseListenerConfiguration(Element eleValue, ParserContext parserContext, MutablePropertyValues configValues) {
    		String destination = eleValue.getAttribute(DESTINATION_ATTRIBUTE);
    		if (!StringUtils.hasText(destination)) {
    			parserContext.getReaderContext().error(
    					"Listener 'destination' attribute contains empty value.", eleValue);
    		}
    		configValues.add("destinationName", destination);
    
    		if (eleValue.hasAttribute(SUBSCRIPTION_ATTRIBUTE)) {
    			String subscription = eleValue.getAttribute(SUBSCRIPTION_ATTRIBUTE);
    			if (!StringUtils.hasText(subscription)) {
    				parserContext.getReaderContext().error(
    						"Listener 'subscription' attribute contains empty value.", eleValue);
    			}
    			configValues.add("subscriptionName", subscription);
    		}
    
    		if (eleValue.hasAttribute(SELECTOR_ATTRIBUTE)) {
    			String selector = eleValue.getAttribute(SELECTOR_ATTRIBUTE);
    			if (!StringUtils.hasText(selector)) {
    				parserContext.getReaderContext().error(
    						"Listener 'selector' attribute contains empty value.", eleValue);
    			}
    			configValues.add("messageSelector", selector);
    		}
    
    		if (eleValue.hasAttribute(CONCURRENCY_ATTRIBUTE)) {
    			String concurrency = eleValue.getAttribute(CONCURRENCY_ATTRIBUTE);
    			if (!StringUtils.hasText(concurrency)) {
    				parserContext.getReaderContext().error(
    						"Listener 'concurrency' attribute contains empty value.", eleValue);
    			}
    			configValues.add("concurrency", concurrency);
    		}
    	}
}
