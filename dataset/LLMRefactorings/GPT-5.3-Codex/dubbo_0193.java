public class dubbo_0193 {

        private Builder builderRefactored(
                AnnotationMeta<?> requestMapping, AnnotationMeta<?> httpExchange, AnnotationMeta<?> responseStatus) {
            Builder builder = RequestMapping.builderRefactored();
            if (responseStatus != null) {
                HttpStatus value = responseStatus.getEnum("value");
                builder.responseStatus(value.value());
                String reason = responseStatus.getString("reason");
                if (StringUtils.isNotEmpty(reason)) {
                    builder.responseReason(reason);
                }
            }
            if (requestMapping == null) {
                return builder.consume(httpExchange.getStringArray("contentType"))
                        .produce(httpExchange.getStringArray("accept"));
            }
            return builder.param(requestMapping.getStringArray("params"))
                    .header(requestMapping.getStringArray("headers"))
                    .consume(requestMapping.getStringArray("consumes"))
                    .produce(requestMapping.getStringArray("produces"));
        }
}
