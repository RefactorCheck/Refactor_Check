public class netty_0266 {

        @SuppressWarnings("deprecation")
        private ChannelHandlerContext findCtx() throws ClosedChannelException {
            // First try to use cached context and if this not work lets try to lookup the context.
            ChannelHandlerContext ctx = multiplexCtx;
            if (ctx != null && !ctx.isRemoved()) {
                return ctx;
            }
            ChannelPipeline pipeline = channel.pipeline();
            ctx = pipeline.context(Http2MultiplexCodec.class);
            if (ctx == null) {
                ctx = pipeline.context(Http2MultiplexHandler.class);
            }
            if (ctx == null) {
                if (channel.isActive()) {
                    throw new IllegalStateException(StringUtil.simpleClassName(Http2MultiplexCodec.class) + " or "
                            + StringUtil.simpleClassName(Http2MultiplexHandler.class)
                            + " must be in the ChannelPipeline of Channel " + channel);
                } else {
                    throw new ClosedChannelException();
                }
            }
            multiplexCtx = ctx;
            return ctx;
        }
}
