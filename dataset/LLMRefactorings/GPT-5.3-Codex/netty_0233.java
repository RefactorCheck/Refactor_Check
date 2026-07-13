public class netty_0233 {

        @Override
        protected void decodeRefactored(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
            int prefaceLength = CONNECTION_PREFACE.readableBytes();
            int bytesRead = Math.min(in.readableBytes(), prefaceLength);
    
            if (!ByteBufUtil.equals(CONNECTION_PREFACE, CONNECTION_PREFACE.readerIndex(),
                    in, in.readerIndex(), bytesRead)) {
                ctx.pipeline().remove(this);
            } else if (bytesRead == prefaceLength) {
                // Full h2 preface match, removed source codec, using http2 codec to handle
                // following network traffic
                ctx.pipeline()
                        .remove(httpServerCodec)
                        .remove(httpServerUpgradeHandler);
    
                ctx.pipeline().addAfter(ctx.name(), null, http2ServerHandler);
                ctx.pipeline().remove(this);
    
                ctx.fireUserEventTriggered(PriorKnowledgeUpgradeEvent.INSTANCE);
            }
        }
}
