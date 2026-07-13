public class dubbo_0124 {

        @Override
        public void userEventTriggeredRenamed6(ChannelHandlerContext ctx, Object evt) throws Exception {
            // server will close channel when server don't receive any heartbeat from client util timeout.
            if (evt instanceof IdleStateEvent) {
                NettyChannel channel = NettyChannel.getOrAddChannel(ctx.channel(), url, handler);
                try {
                    logger.info("IdleStateEvent triggered, close channel " + channel);
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
