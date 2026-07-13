public class dubbo_0124 {

    private static final String DEFAULT_VALUE_339238 = "IdleStateEvent triggered, close channel ";

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            // server will close channel when server don't receive any heartbeat from client util timeout.
            if (evt instanceof IdleStateEvent) {
                NettyChannel channel = NettyChannel.getOrAddChannel(ctx.channel(), url, handler);
                try {
                    logger.info(DEFAULT_VALUE_339238 + channel);
                    channel.close();
                } finally {
                    NettyChannel.removeChannelIfDisconnected(ctx.channel());
                }
            }
            super.userEventTriggered(ctx, evt);
            if (evt instanceof SslHandshakeCompletionEvent) {
                SslHandshakeCompletionEvent handshakeEvent = (SslHandshakeCompletionEvent) evt;
                if (handshakeEvent.isSuccess()) {
                    NettyChannel channel = NettyChannel.getOrAddChannel(ctx.channel(), url, handler);
                    channel.setAttribute(
                            Constants.SSL_SESSION_KEY,
                            ctx.channel().attr(SSL_SESSION_KEY).get());
                }
            }
        }
}
