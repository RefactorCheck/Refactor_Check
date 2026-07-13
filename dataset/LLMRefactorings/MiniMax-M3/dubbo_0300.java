public class dubbo_0300 {

        @Override
        public ConsumesCondition match(HttpRequest request) {
            if (expressions.isEmpty()) {
                return null;
            }
    
            String contentType = request.contentType();
            MediaTypeExpression mediaType = contentType == null ? DEFAULT : MediaTypeExpression.parse(contentType);
            List<MediaTypeExpression> result = findMatchingExpressions(mediaType);
            return result == null ? null : new ConsumesCondition(result);
        }
    
        private List<MediaTypeExpression> findMatchingExpressions(MediaTypeExpression mediaType) {
            List<MediaTypeExpression> result = null;
            for (int i = 0, size = expressions.size(); i < size; i++) {
                MediaTypeExpression expression = expressions.get(i);
                if (expression.match(mediaType)) {
                    if (result == null) {
                        result = new ArrayList<>();
                    }
                    result.add(expression);
                }
            }
            return result;
        }
}
