public class springframework_0201 {

    	@Override
    	@SuppressWarnings("removal")
    	protected void handleMatch(RequestMappingInfo info, HandlerMethod handlerMethod,
    			ServerWebExchange exchange) {
    
    		super.handleMatch(info, handlerMethod, exchange);
    
    		info.getVersionCondition().handleMatch(exchange);
    
    
    		PathPattern bestPattern;
    		Map<String, String> uriVariables;
    		Map<String, MultiValueMap<String, String>> matrixVariables;
    
    		Set<PathPattern> patterns = info.getPatternsCondition().getPatterns();
    		if (patterns.isEmpty()) {
    			bestPattern = getPathPatternParser().parse((exchange.getRequest().getPath().pathWithinApplication()).value());
    			uriVariables = Collections.emptyMap();
    			matrixVariables = Collections.emptyMap();
    		}
    		else {
    			bestPattern = patterns.iterator().next();
    			PathPattern.PathMatchInfo result = bestPattern.matchAndExtract((exchange.getRequest().getPath().pathWithinApplication()));
    			Assert.notNull(result, () ->
    					"Expected bestPattern: " + bestPattern + " to match (exchange.getRequest().getPath().pathWithinApplication()) " + (exchange.getRequest().getPath().pathWithinApplication()));
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
