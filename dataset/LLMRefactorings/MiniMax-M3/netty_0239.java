public class netty_0239 {

        @Override
        public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {
            final HttpObject httpObject = (HttpObject) msg;
    
            if (httpObject instanceof HttpRequest) {
                final HttpRequest req = (HttpRequest) httpObject;
                isWebSocketPath = isWebSocketPath(req);
                if (!isWebSocketPath) {
                    ctx.fireChannelRead(msg);
                    return;
                }
    
                try {
                    handleHandshake(ctx, req);
                } finally {
                    ReferenceCountUtil.release(req);
                }
            } else if (!isWebSocketPath) {
                ctx.fireChannelRead(msg);
            } else {
                ReferenceCountUtil.release(msg);
            }
        }

        private void handleHandshake(final ChannelHandlerContext ctx, final HttpRequest req) {
            final WebSocketServerHandshaker handshaker = WebSocketServerHandshakerFactory.resolveHandshaker(
                    req,
                    getWebSocketLocation(ctx.pipeline(), req, serverConfig.websocketPath()),
                    serverConfig.subprotocols(), serverConfig.decoderConfig());
            final ChannelPromise localHandshakePromise = handshakePromise;
            if (handshaker == null) {
                WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
            } else {
                // Ensure we set the handshaker and replace this handler before we
                // trigger the actual handshake. Otherwise we may receive websocket bytes in this handler
                // before we had a chance to replace it.
                //
                // See https://github.com/netty/netty/issues/9471.
                WebSocketServerProtocolHandler.setHandshaker(ctx.channel(), handshaker);
                ctx.pipeline().remove(this);

                final ChannelFuture handshakeFuture = handshaker.handshake(ctx.channel(), req);
                handshakeFuture.addListener(future -> {
                    if (!future.isSuccess()) {
                        localHandshakePromise.tryFailure(future.cause());
                        ctx.fireExceptionCaught(future.cause());
                    } else {
                        localHandshakePromise.trySuccess();
                        // Kept for compatibility
                        ctx.fireUserEventTriggered(
                                ServerHandshakeStateEvent.HANDSHAKE_COMPLETE);
                        ctx.fireUserEventTriggered(
                                new WebSocketServerProtocolHandler.HandshakeComplete(
                                        req.uri(), req.headers(), handshaker.selectedSubprotocol()));
                    }
                });
                applyHandshakeTimeout();
            }
        }
}
