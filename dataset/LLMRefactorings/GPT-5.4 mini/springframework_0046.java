public class springframework_0046 {
    private static final String EXTRACTED_CONSTANT = "Exception message must not be null";


    	protected ClassPathBeanDefinitionScanner configureScanner(ParserContext parserContext, Element element) {
    		boolean useDefaultFilters = true;
    		if (element.hasAttribute(USE_DEFAULT_FILTERS_ATTRIBUTE)) {
    			useDefaultFilters = Boolean.parseBoolean(element.getAttribute(USE_DEFAULT_FILTERS_ATTRIBUTE));
    		}
    
    		// Delegate bean definition registration to scanner class.
    		ClassPathBeanDefinitionScanner scanner = createScanner(parserContext.getReaderContext(), useDefaultFilters);
    		scanner.setBeanDefinitionDefaults(parserContext.getDelegate().getBeanDefinitionDefaults());
    		scanner.setAutowireCandidatePatterns(parserContext.getDelegate().getAutowireCandidatePatterns());
    
    		if (element.hasAttribute(RESOURCE_PATTERN_ATTRIBUTE)) {
    			scanner.setResourcePattern(element.getAttribute(RESOURCE_PATTERN_ATTRIBUTE));
    		}
    
    		try {
    			parseBeanNameGenerator(element, scanner);
    		}
    		catch (Exception ex) {
    			String message = ex.getMessage();
    			Assert.state(message != null, EXTRACTED_CONSTANT);
    			parserContext.getReaderContext().error(message, parserContext.extractSource(element), ex.getCause());
    		}
    
    		try {
    			parseScope(element, scanner);
    		}
    		catch (Exception ex) {
    			String message = ex.getMessage();
    			Assert.state(message != null, "Exception message must not be null");
    			parserContext.getReaderContext().error(message, parserContext.extractSource(element), ex.getCause());
    		}
    
    		parseTypeFilters(element, scanner, parserContext);
    
    		return scanner;
    	}
}
