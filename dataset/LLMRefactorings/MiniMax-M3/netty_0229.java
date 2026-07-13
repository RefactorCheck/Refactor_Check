public class netty_0229 {

        private CharSequence getSettingsHeaderValue(ChannelHandlerContext ctx) {
            ByteBuf buf = null;
            ByteBuf encodedBuf = null;
            try {
                buf = writeSettingsPayload(ctx);
                encodedBuf = Base64.encode(buf, URL_SAFE);
                return encodedBuf.toString(UTF_8);
            } finally {
                release(buf);
                release(encodedBuf);
            }
        }
        
        private ByteBuf writeSettingsPayload(ChannelHandlerContext ctx) {
            Http2Settings settings = connectionHandler.decoder().localSettings();
            int payloadLength = SETTING_ENTRY_LENGTH * settings.size();
            ByteBuf buf = ctx.alloc().buffer(payloadLength);
            for (CharObjectMap.PrimitiveEntry<Long> entry : settings.entries()) {
                buf.writeChar(entry.key());
                buf.writeInt(entry.value().intValue());
            }
            return buf;
        }
}
