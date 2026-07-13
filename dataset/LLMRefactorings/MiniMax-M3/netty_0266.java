public class netty_0266 {

        @SuppressWarnings("deprecation")
        private ChannelHandlerContext findCtx() throws ClosedChannelException {
            ChannelHandlerContext ctx = multiplexCtx;
            if (ctx != null && !ctx.isRemoved()) {
                return ctx;
            }
            ctx = lookupContext();
            multiplexCtx = ctx;
            return ctx;
        }

        private ChannelHandlerContext lookupContext() throws ClosedChannelException {
            ChannelPipeline pipeline = channel.pipeline();
            ChannelHandlerContext ctx = pipeline.context(Http2MultiplexCodec.class);
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
            return ctx;
        }
}
