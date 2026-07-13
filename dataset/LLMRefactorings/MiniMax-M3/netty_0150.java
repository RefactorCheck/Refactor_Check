public class netty_0150 {

        @Override
        protected final void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
            if (version == -1) {
                if ((version = findVersion(in)) == -1) {
                    return;
                }
            }

            ByteBuf decoded = decodeByVersion(ctx, in);

            if (decoded != null) {
                finished = true;
                try {
                    out.add(createMessage(decoded));
                } catch (HAProxyProtocolException e) {
                    fail(ctx, null, e);
                }
            }
        }

        private ByteBuf decodeByVersion(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
            if (version == 1) {
                return decodeLine(ctx, in);
            } else {
                return decodeStruct(ctx, in);
            }
        }

        private Object createMessage(ByteBuf decoded) {
            if (version == 1) {
                return HAProxyMessage.decodeHeader(decoded.toString(CharsetUtil.US_ASCII));
            } else {
                return HAProxyMessage.decodeHeader(decoded);
            }
        }
}
