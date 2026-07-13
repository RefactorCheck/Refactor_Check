public class dubbo_0147 {

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
            org.jboss.netty.channel.Channel ch = ctx.getChannel();
            NettyChannel channel = NettyChannel.getOrAddChannel(ch, url, handler);
            try {
                handler.caught(channel, e.getCause());
            } finally {
                NettyChannel.removeChannelIfDisconnected(ch);
            }

            logUnexpectedException(channel, ch, e.getCause());
        }

        private void logUnexpectedException(NettyChannel channel, org.jboss.netty.channel.Channel ch, Throwable cause) {
            if (logger.isWarnEnabled()) {
                logger.warn(
                        TRANSPORT_UNEXPECTED_EXCEPTION,
                        "",
                        "",
                        channel == null
                                ? String.format("The connection %s has exception.", ch)
                                : String.format(
                                        "The connection %s between %s and %s has exception.",
                                        ch, channel.getRemoteAddress(), channel.getLocalAddress()),
                        cause);
            }
        }
}
