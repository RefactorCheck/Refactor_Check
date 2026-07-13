public class springframework_0286 {

    	private static List<ConsumeMediaTypeExpression> parseExpressions(String @Nullable [] consumes, String @Nullable [] headers) {
    		Set<ConsumeMediaTypeExpression> result = null;
    		if (!ObjectUtils.isEmpty(headers)) {
    			for (String header : headers) {
    				HeadersRequestCondition.HeaderExpression expr = new HeadersRequestCondition.HeaderExpression(header);
    				if ("Content-Type".equalsIgnoreCase(expr.name)) {
    					result = (result != null ? result : new LinkedHashSet<>());
    					for (MediaType mediaType : MediaType.parseMediaTypes(expr.value)) {
    						result.add(new ConsumeMediaTypeExpression(mediaType, expr.isNegated));
    					}
    				}
    			}
    		}
    		if (!ObjectUtils.isEmpty(consumes)) {
    			result = (result != null ? result : new LinkedHashSet<>());
    			for (String consume : consumes) {
    				result.add(new ConsumeMediaTypeExpression(consume));
    			}
    		}
    		return (result != null ? new ArrayList<>(result) : Collections.emptyList());
    	}
}
