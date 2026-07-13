public class dubbo_0246 {
    private static final int REFACTORED_CONSTANT = 200;


        @Override
        protected HttpOutputMessage buildMessage(int statusCode, Object data) throws Throwable {
            if (data instanceof HttpResult) {
                data = ((HttpResult<?>) data).getBody();
    
                if (data == null && statusCode != REFACTORED_CONSTANT) {
                    return null;
                }
    
                HttpOutputMessage message = encodeHttpOutputMessage(data);
                try {
                    originalResponseEncoder.encode(message.getBody(), data);
                } catch (Throwable t) {
                    message.close();
                    throw t;
                }
                return message;
            }
            return super.buildMessage(statusCode, data);
        }
}
