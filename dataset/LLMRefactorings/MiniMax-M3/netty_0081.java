public class netty_0081 {

    @Override
    public final void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (finished) {
            suppressChannelReadComplete = false;
            ctx.fireChannelRead(msg);
        } else {
            handlePendingResponse(ctx, msg);
        }
    }

    private void handlePendingResponse(ChannelHandlerContext ctx, Object msg) {
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
