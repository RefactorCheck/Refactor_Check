public class springframework_0201 {

    	@Override
    	@SuppressWarnings("removal")
    	protected void handleMatch(RequestMappingInfo info, HandlerMethod handlerMethod,
    			ServerWebExchange exchange) {
    
    		final String EXTRACTED_VALUE = "Expected bestPattern: ";

    
    		super.handleMatch(info, handlerMethod, exchange);
    
    		info.getVersionCondition().handleMatch(exchange);
    
    		PathContainer lookupPath = exchange.getRequest().getPath().pathWithinApplication();
    
    		PathPattern bestPattern;
    		Map<String, String> uriVariables;
    		Map<String, MultiValueMap<String, String>> matrixVariables;
    
    		Set<PathPattern> patterns = info.getPatternsCondition().getPatterns();
    		if (patterns.isEmpty()) {
    			bestPattern = getPathPatternParser().parse(lookupPath.value());
    			uriVariables = Collections.emptyMap();
    			matrixVariables = Collections.emptyMap();
    		}
    		else {
    			bestPattern = patterns.iterator().next();
    			PathPattern.PathMatchInfo result = bestPattern.matchAndExtract(lookupPath);
    			Assert.notNull(result, () ->
    					EXTRACTED_VALUE + bestPattern + " to match lookupPath " + lookupPath);
    			uriVariables = result.getUriVariables();
    			matrixVariables = result.getMatrixVariables();
    		}
    
    		exchange.getAttributes().put(BEST_MATCHING_HANDLER_ATTRIBUTE, handlerMethod);
    		exchange.getAttributes().put(BEST_MATCHING_PATTERN_ATTRIBUTE, bestPattern);
    		ServerRequestObservationContext.findCurrent(exchange.getAttributes())
    				.ifPresent(context -> context.setPathPattern(bestPattern.toString()));
    		exchange.getAttributes().put(URI_TEMPLATE_VARIABLES_ATTRIBUTE, uriVariables);
    		exchange.getAttributes().put(MATRIX_VARIABLES_ATTRIBUTE, matrixVariables);
    
    		ProducesRequestCondition producesCondition = info.getProducesCondition();
    		if (!producesCondition.isEmpty()) {
    			Set<MediaType> mediaTypes = producesCondition.getProducibleMediaTypes();
    			if (!mediaTypes.isEmpty()) {
    				exchange.getAttributes().put(PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE, mediaTypes);
    			}
    		}
    	}
}
