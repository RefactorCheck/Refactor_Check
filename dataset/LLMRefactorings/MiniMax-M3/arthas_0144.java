public class arthas_0144 {

      @Override
      public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {
          scheduleHandshakeComplete(ctx);
        } else if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
          WebSocketServerProtocolHandler.HandshakeComplete handshakeComplete =
              (WebSocketServerProtocolHandler.HandshakeComplete) evt;
          handleHandshakeComplete(ctx, handshakeComplete.requestUri());
        } else if (evt instanceof IdleStateEvent) {
          ctx.writeAndFlush(new PingWebSocketFrame());
        } else {
          super.userEventTriggered(ctx, evt);
        }
      }

      private void scheduleHandshakeComplete(ChannelHandlerContext ctx) {
        ctx.executor().execute(new Runnable() {
          @Override
          public void run() {
            handleHandshakeComplete(ctx, null);
          }
        });
      }
}
