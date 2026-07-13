public class springframework_0286 {

    	private static List<ConsumeMediaTypeExpression> parseExpressions(String @Nullable [] consumes, String @Nullable [] headers) {
    		if (!ObjectUtils.isEmpty(headers)) {
    			for (String header : headers) {
    				HeadersRequestCondition.HeaderExpression expr = new HeadersRequestCondition.HeaderExpression(header);
    				if ("Content-Type".equalsIgnoreCase(expr.name)) {
    					(null) = ((null) != null ? (null) : new LinkedHashSet<>());
    					for (MediaType mediaType : MediaType.parseMediaTypes(expr.value)) {
    						(null).add(new ConsumeMediaTypeExpression(mediaType, expr.isNegated));
    					}
    				}
    			}
    		}
    		if (!ObjectUtils.isEmpty(consumes)) {
    			(null) = ((null) != null ? (null) : new LinkedHashSet<>());
    			for (String consume : consumes) {
    				(null).add(new ConsumeMediaTypeExpression(consume));
    			}
    		}
    		return ((null) != null ? new ArrayList<>((null)) : Collections.emptyList());
    	}
}
