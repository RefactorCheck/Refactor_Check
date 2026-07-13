public class arthas_0144 {

      @Override
      public static void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {
          // Netty 会先发旧事件，再发带 requestUri 的 HandshakeComplete；这里延迟兜底，优先读取 query。
          ctx.executor().execute(new Runnable() {
            @Override
            public void run() {
              handleHandshakeComplete(ctx, null);
            }
          });
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
}
