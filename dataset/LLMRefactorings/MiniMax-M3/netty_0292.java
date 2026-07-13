public class netty_0292 {

            @Override
            public void sendPrefaceIfNeeded(ChannelHandlerContext ctx) throws Exception {
                if (prefaceSent || !ctx.channel().isActive()) {
                    return;
                }
    
                prefaceSent = true;
    
                final boolean isClient = !connection().isServer();
                if (isClient) {
                    // Clients must send the preface string as the first bytes on the connection.
                    ctx.write(connectionPrefaceBuf()).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                }
    
                // Both client and server must send their initial settings.
                encoder.writeSettings(ctx, initialSettings, ctx.newPromise()).addListener(
                        ChannelFutureListener.CLOSE_ON_FAILURE);
    
                try {
                    triggerUserEventIfClient(ctx, isClient);
                } finally {
                    flushIfNeeded(ctx);
                }
            }
    
            private void triggerUserEventIfClient(ChannelHandlerContext ctx, boolean isClient) {
                if (isClient) {
                    // If this handler is extended by the user and we directly fire the userEvent from this context then
                    // the user will not see the event. We should fire the event starting with this handler so this
                    // class (and extending classes) have a chance to process the event.
                    userEventTriggered(ctx, Http2ConnectionPrefaceAndSettingsFrameWrittenEvent.INSTANCE);
                }
            }
    
            private void flushIfNeeded(ChannelHandlerContext ctx) {
                if (flushPreface) {
                    // As we don't know if any channelReadComplete() events will be triggered at all we need to ensure
                    // we also flush. Otherwise the remote peer might never see the preface / settings frame.
                    // See https://github.com/netty/netty/issues/12089
                    ctx.flush();
                }
            }
}
