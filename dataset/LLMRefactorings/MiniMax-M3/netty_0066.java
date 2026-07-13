public class netty_0066 {

    private boolean handleNewChannel(ChannelHandlerContext ctx) throws Exception {
        @SuppressWarnings("unchecked")
        T remoteAddress = (T) ctx.channel().remoteAddress();
    
        if (remoteAddress == null) {
            return false;
        }
    
        ctx.pipeline().remove(this);
    
        if (accept(ctx, remoteAddress)) {
            channelAccepted(ctx, remoteAddress);
        } else {
            rejectChannel(ctx, remoteAddress);
        }
    
        return true;
    }

    private void rejectChannel(ChannelHandlerContext ctx, T remoteAddress) {
        ChannelFuture rejectedFuture = channelRejected(ctx, remoteAddress);
        if (rejectedFuture != null) {
            rejectedFuture.addListener(ChannelFutureListener.CLOSE);
        } else {
            ctx.close();
        }
    }
}
