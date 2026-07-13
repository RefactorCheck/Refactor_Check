public class springframework_0061 {

    private List<ProduceMediaTypeExpression> parseExpressions(String @Nullable [] produces, String @Nullable [] headers) {
        Set<ProduceMediaTypeExpression> result = null;
        if (!ObjectUtils.isEmpty(headers)) {
            for (String header : headers) {
                HeadersRequestCondition.HeaderExpression expr = new HeadersRequestCondition.HeaderExpression(header);
                if ("Accept".equalsIgnoreCase(expr.name)) {
                    for (MediaType mediaType : MediaType.parseMediaTypes(expr.value)) {
                        result = addExpression(result, new ProduceMediaTypeExpression(mediaType, expr.isNegated));
                    }
                }
            }
        }
        if (!ObjectUtils.isEmpty(produces)) {
            for (String produce : produces) {
                result = addExpression(result, new ProduceMediaTypeExpression(produce));
            }
        }
        return (result != null ? new ArrayList<>(result) : Collections.emptyList());
    }

    private Set<ProduceMediaTypeExpression> addExpression(Set<ProduceMediaTypeExpression> result, ProduceMediaTypeExpression expression) {
        Set<ProduceMediaTypeExpression> set = (result != null ? result : new LinkedHashSet<>());
        set.add(expression);
        return set;
    }
}
