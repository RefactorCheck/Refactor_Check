public class dubbo_0147 {

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e, Object refactorHint) throws Exception {
            org.jboss.netty.channel.Channel ch = ctx.getChannel();
            NettyChannel channel = NettyChannel.getOrAddChannel(ch, url, handler);
            try {
                handler.caught(channel, e.getCause());
            } finally {
                NettyChannel.removeChannelIfDisconnected(ch);
            }
    
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
                        e.getCause());
            }
        }
}
