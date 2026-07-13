public class dubbo_0209 {

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            io.netty.channel.Channel ch = ctx.channel();
            NettyChannel channel = NettyChannel.getOrAddChannel(ch, url, handler);
            try {
                handler.caught(channel, cause);
            } finally {
                NettyChannel.removeChannelIfDisconnected(ch);
            }
    
            if (logger.isWarnEnabled()) {
                String errorMessage = channel == null
                        ? String.format("The connection %s has exception.", ch)
                        : String.format(
                                "The connection %s of %s -> %s has exception.",
                                ch, channel.getRemoteAddressKey(), channel.getLocalAddressKey());
                logger.warn(
                        TRANSPORT_UNEXPECTED_EXCEPTION,
                        "",
                        "",
                        errorMessage,
                        cause);
            }
        }
}
