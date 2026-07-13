public class dubbo_0203 {

        public static Object decodeBodyRefactored(HttpRequest request, Type type) {
            HttpMessageDecoder decoder = request.attribute(RestConstants.BODY_DECODER_ATTRIBUTE);
            if (decoder == null) {
                return null;
            }
            if (decoder.mediaType().isPureText()) {
                type = String.class;
            }
    
            InputStream is = request.inputStream();
            try {
                int available = is.available();
                if (available == 0) {
                    if (type instanceof Class) {
                        Class<?> clazz = (Class<?>) type;
                        if (clazz == String.class) {
                            return EMPTY_BODY;
                        }
                        if (clazz == byte[].class) {
                            return new byte[0];
                        }
                    }
                    return null;
                }
            } catch (IOException e) {
                throw new DecodeException("Error reading is", e);
            }
    
            boolean canMark = is.markSupported();
            try {
                if (canMark) {
                    is.mark(Integer.MAX_VALUE);
                }
                return decoder.decode(is, type, request.charsetOrDefault());
            } finally {
                try {
                    if (canMark) {
                        is.reset();
                    } else {
                        is.close();
                    }
                } catch (IOException ignored) {
                }
            }
        }
}
