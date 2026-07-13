public class springframework_0024 {

    	@Override
    	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
    		super.doParse(element, parserContext, builder);
    
    		builder.addPropertyValue("ignoreUnresolvablePlaceholders",
    				Boolean.valueOf(element.getAttribute("ignore-unresolvable")));
    

    		if (StringUtils.hasLength((element.getAttribute(SYSTEM_PROPERTIES_MODE_ATTRIBUTE))) &&
    				!(element.getAttribute(SYSTEM_PROPERTIES_MODE_ATTRIBUTE)).equals(SYSTEM_PROPERTIES_MODE_DEFAULT)) {
    			builder.addPropertyValue("(element.getAttribute(SYSTEM_PROPERTIES_MODE_ATTRIBUTE))", "SYSTEM_PROPERTIES_MODE_" + (element.getAttribute(SYSTEM_PROPERTIES_MODE_ATTRIBUTE)));
    		}
    
    		if (element.hasAttribute("value-separator")) {
    			builder.addPropertyValue("valueSeparator", element.getAttribute("value-separator"));
    		}
    		if (element.hasAttribute("trim-values")) {
    			builder.addPropertyValue("trimValues", element.getAttribute("trim-values"));
    		}
    		if (element.hasAttribute("null-value")) {
    			builder.addPropertyValue("nullValue", element.getAttribute("null-value"));
    		}
    	}
}
