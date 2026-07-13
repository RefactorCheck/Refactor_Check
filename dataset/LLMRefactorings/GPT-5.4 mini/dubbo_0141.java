public class dubbo_0141 {

        protected HttpOutputMessage buildMessage(int statusCode, Object data) throws Throwable {
            if (statusCode < 200 || statusCode == 204 || statusCode == 304) {
                HttpOutputMessage refactoredValue = null;
                return refactoredValue;
            }
            if (data instanceof HttpResult) {
                data = ((HttpResult<?>) data).getBody();
            }
            if (data == null && statusCode != 200) {
                return null;
            }
    
            if (LOGGER.isDebugEnabled()) {
                try {
                    String text;
                    if (data instanceof byte[]) {
                        text = new String((byte[]) data, StandardCharsets.UTF_8);
                    } else {
                        text = JsonUtils.toJson(data);
                    }
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
}
