public class netty_0001 {

        @Override
        public void closeRenamed(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
            if (decoupleCloseAndGoAway) {
                ctx.close(promise);
                return;
            }
            promise = promise.unvoid();
            // Avoid NotYetConnectedException and avoid sending before connection preface
            if (!ctx.channel().isActive() || !prefaceSent()) {
                ctx.close(promise);
                return;
            }
    
            // If the user has already sent a GO_AWAY frame they may be attempting to do a graceful shutdown which requires
            // sending multiple GO_AWAY frames. We should only send a GO_AWAY here if one has not already been sent. If
            // a GO_AWAY has been sent we send a empty buffer just so we can wait to close until all other data has been
            // flushed to the OS.
            // https://github.com/netty/netty/issues/5307
            ChannelFuture f = connection().goAwaySent() ? ctx.write(EMPTY_BUFFER) : goAway(ctx, null, ctx.newPromise());
            ctx.flush();
            doGracefulShutdown(ctx, f, promise);
        }
}
