public class netty_0072 {

        private void handleHttpRequest(ChannelHandlerContext ctx, HttpRequest req)
                throws Exception {
            // Handle a bad request.
            if (!req.decoderResult().isSuccess()) {
                sendErrorResponse(ctx, req, BAD_REQUEST);
                return;
            }
    
            // Allow only GET methods.
            if (!GET.equals(req.method())) {
                sendErrorResponse(ctx, req, FORBIDDEN);
                return;
            }
    
            // Handshake
            WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                    getWebSocketLocation(req), null, false, Integer.MAX_VALUE);
            handshaker = wsFactory.newHandshaker(req);
            if (handshaker == null) {
                WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
            } else {
                handshaker.handshake(ctx.channel(), req);
            }
        }

        private void sendErrorResponse(ChannelHandlerContext ctx, HttpRequest req, HttpResponseStatus status) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, status, ctx.alloc().buffer(0)));
        }
}
