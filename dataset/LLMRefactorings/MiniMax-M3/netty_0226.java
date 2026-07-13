public class netty_0226 {

        private static void encodeHeaders(StompHeadersSubframe frame, ByteBuf buf) {
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
                writeHeader(buf, headerKey, entry.getValue(), shouldEscape);
            }
    
            buf.writeByte(StompConstants.LF);
        }

        private static void writeHeader(ByteBuf buf, CharSequence key, CharSequence value, boolean shouldEscape) {
            ByteBufUtil.writeUtf8(buf, key);
            buf.writeByte(StompConstants.COLON);
            CharSequence escapedValue = shouldEscape ? escape(value) : value;
            ByteBufUtil.writeUtf8(buf, escapedValue);
            buf.writeByte(StompConstants.LF);
        }
}
