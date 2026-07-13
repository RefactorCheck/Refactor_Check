public class springframework_0061 {

    	private List<ProduceMediaTypeExpression> parseExpressions(String @Nullable [] produces, String @Nullable [] headers) {
    		Set<ProduceMediaTypeExpression> result = null;
    		if (!ObjectUtils.isEmpty(headers)) {
    			for (String header : headers) {
    				HeadersRequestCondition.HeaderExpression expr = new HeadersRequestCondition.HeaderExpression(header);
    				if ("Accept".equalsIgnoreCase(expr.name)) {
    					for (MediaType mediaType : MediaType.parseMediaTypes(expr.value)) {
    						result = (result != null ? result : new LinkedHashSet<>());
    						result.add(new ProduceMediaTypeExpression(mediaType, expr.isNegated));
    					}
    				}
    			}
    		}
    		if (!ObjectUtils.isEmpty(produces)) {
    			for (String produce : produces) {
    				result = (result != null ? result : new LinkedHashSet<>());
    				result.add(new ProduceMediaTypeExpression(produce));
    			}
    		}
    		return (result != null ? new ArrayList<>(result) : Collections.emptyList());
    	}
}
