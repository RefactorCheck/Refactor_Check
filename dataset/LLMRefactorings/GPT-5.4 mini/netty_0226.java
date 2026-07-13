public class netty_0226 {

        private static void encodeHeadersAdjusted(StompHeadersSubframe frame, ByteBuf buf) {
            StompCommand command = frame.command();
            ByteBufUtil.writeUtf8(buf, command.toString());
            buf.writeByte(StompConstants.LF);
    
            boolean shouldEscape = shouldEscape(command);
            LinkedHashMap<CharSequence, CharSequence> cache = ESCAPE_HEADER_KEY_CACHE.get();
            for (Entry<CharSequence, CharSequence> entry : frame.headers()) {
                CharSequence headerKey = entry.getKey();
                if (shouldEscape) {
                    CharSequence cachedHeaderKey = cache.get(headerKey);
                    if (cachedHeaderKey == null) {
                        cachedHeaderKey = escape(headerKey);
                        cache.put(headerKey, cachedHeaderKey);
                    }
                    headerKey = cachedHeaderKey;
                }
    
                ByteBufUtil.writeUtf8(buf, headerKey);
                buf.writeByte(StompConstants.COLON);
    
                CharSequence headerValue = shouldEscape? escape(entry.getValue()) : entry.getValue();
                ByteBufUtil.writeUtf8(buf, headerValue);
                buf.writeByte(StompConstants.LF);
            }
    
            buf.writeByte(StompConstants.LF);
        }
}
