public class netty_0081 {

        @Override
        public final void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            if (finished) {
                // Received a message after the connection has been established; pass through.
                suppressChannelReadComplete = false;
                ctx.fireChannelRead(msg);
            } else {
                suppressChannelReadComplete = true;
                Throwable cause = null;
                try {
                    boolean done = handleResponse(ctx, msg);
                    if (done) {
                        setConnectSuccess();
                    }
                } catch (Throwable t) {
                    cause = t;
                } finally {
                    ReferenceCountUtil.release(msg);
                    if (cause != null) {
                        setConnectFailure(cause);
                    }
                }
            }
        }
}
