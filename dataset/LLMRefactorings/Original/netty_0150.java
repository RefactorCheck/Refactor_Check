public class netty_0150 {

        @Override
        protected final void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
            // determine the specification version
            if (version == -1) {
                if ((version = findVersion(in)) == -1) {
                    return;
                }
            }
    
            ByteBuf decoded;
    
            if (version == 1) {
                decoded = decodeLine(ctx, in);
            } else {
                decoded = decodeStruct(ctx, in);
            }
    
            if (decoded != null) {
                finished = true;
                try {
                    if (version == 1) {
                        out.add(HAProxyMessage.decodeHeader(decoded.toString(CharsetUtil.US_ASCII)));
                    } else {
                        out.add(HAProxyMessage.decodeHeader(decoded));
                    }
                } catch (HAProxyProtocolException e) {
                    fail(ctx, null, e);
                }
            }
        }
}
