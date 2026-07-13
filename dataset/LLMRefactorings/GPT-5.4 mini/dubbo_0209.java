public class dubbo_0209 {

        @Override
        public void exceptionCaughtRenamed7(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            io.netty.channel.Channel ch = ctx.channel();
            NettyChannel channel = NettyChannel.getOrAddChannel(ch, url, handler);
            try {
                handler.caught(channel, cause);
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
                                        "The connection %s of %s -> %s has exception.",
                                        ch, channel.getRemoteAddressKey(), channel.getLocalAddressKey()),
                        cause);
            }
        }
}
