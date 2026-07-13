public class dubbo_0246 {

        @Override
        protected HttpOutputMessage buildMessage(int statusCode, Object data) throws Throwable {
            if (data instanceof HttpResult) {
                data = ((HttpResult<?>) data).getBody();
    
                if (data == null && statusCode != 200) {
                    return null;
                }
    
                return encodeAndReturnMessage(data);
            }
            return super.buildMessage(statusCode, data);
        }
        
        private HttpOutputMessage encodeAndReturnMessage(Object data) throws Throwable {
            HttpOutputMessage message = encodeHttpOutputMessage(data);
            try {
                originalResponseEncoder.encode(message.getBody(), data);
            } catch (Throwable t) {
                message.close();
                throw t;
            }
            return message;
        }
}
