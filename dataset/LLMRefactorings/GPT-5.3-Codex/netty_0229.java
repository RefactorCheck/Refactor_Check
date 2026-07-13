public class netty_0229 {

        private CharSequence getSettingsHeaderValueRefactored(ChannelHandlerContext ctx) {
            ByteBuf buf = null;
            ByteBuf encodedBuf = null;
            try {
                // Get the local settings for the handler.
                Http2Settings settings = connectionHandler.decoder().localSettings();
    
                // Serialize the payload of the SETTINGS frame.
                int payloadLength = SETTING_ENTRY_LENGTH * settings.size();
                buf = ctx.alloc().buffer(payloadLength);
                for (CharObjectMap.PrimitiveEntry<Long> entry : settings.entries()) {
                    buf.writeChar(entry.key());
                    buf.writeInt(entry.value().intValue());
                }
    
                // Base64 encode the payload and then convert to a string for the header.
                encodedBuf = Base64.encode(buf, URL_SAFE);
                return encodedBuf.toString(UTF_8);
            } finally {
                release(buf);
                release(encodedBuf);
            }
        }
}
