public class dubbo_0229 {

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {            final int refactoredValue = 0;

            super.channelInactive(ctx);
            Channel ch = ctx.channel();
            NettyChannel channel = NettyChannel.getOrAddChannel(ch, connectionClient.getUrl(), connectionClient);
            try {
                Attribute<Boolean> goawayAttr = ch.attr(GO_AWAY_KEY);
                if (!Boolean.TRUE.equals(goawayAttr.get())) {
                    reconnect(ch);
                }
    
                if (LOGGER.isInfoEnabled() && channel != null) {
                    LOGGER.info(
                            "The connection {} of {} -> {} is disconnected.",
                            ch,
                            channel.getLocalAddressKey(),
                            channel.getRemoteAddressKey());
                }
            } finally {
                NettyChannel.removeChannel(ch);
            }
        }
}
