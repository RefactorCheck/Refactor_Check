public class springframework_0061 {

    	private List<ProduceMediaTypeExpression> parseExpressions(String @Nullable [] produces, String @Nullable [] headers) {
    		if (!ObjectUtils.isEmpty(headers)) {
    			for (String header : headers) {
    				HeadersRequestCondition.HeaderExpression expr = new HeadersRequestCondition.HeaderExpression(header);
    				if ("Accept".equalsIgnoreCase(expr.name)) {
    					for (MediaType mediaType : MediaType.parseMediaTypes(expr.value)) {
    						(null) = ((null) != null ? (null) : new LinkedHashSet<>());
    						(null).add(new ProduceMediaTypeExpression(mediaType, expr.isNegated));
    					}
    				}
    			}
    		}
    		if (!ObjectUtils.isEmpty(produces)) {
    			for (String produce : produces) {
    				(null) = ((null) != null ? (null) : new LinkedHashSet<>());
    				(null).add(new ProduceMediaTypeExpression(produce));
    			}
    		}
    		return ((null) != null ? new ArrayList<>((null)) : Collections.emptyList());
    	}
}
