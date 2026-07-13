public class dubbo_0141 {

        protected HttpOutputMessage buildMessage(int statusCode, Object data) throws Throwable {
            if (statusCode < 200 || statusCode == 204 || statusCode == 304) {
                return null;
            }
            if (data instanceof HttpResult) {
                data = ((HttpResult<?>) data).getBody();
            }
            if (data == null && statusCode != 200) {
                return null;
            }
    
            if (LOGGER.isDebugEnabled()) {
                try {
                    String text = convertToText(data);
                    LOGGER.debug("Http response body sent: '{}' by [{}]", text, httpChannel);
                } catch (Throwable ignored) {
                }
            }
            HttpOutputMessage message = encodeHttpOutputMessage(data);
            try {
                preOutputMessage(message);
                responseEncoder.encode(message.getBody(), data);
            } catch (Throwable t) {
                message.close();
                throw t;
            }
            return message;
        }

        private String convertToText(Object data) {
            if (data instanceof byte[]) {
                return new String((byte[]) data, StandardCharsets.UTF_8);
            }
            return JsonUtils.toJson(data);
        }
}
