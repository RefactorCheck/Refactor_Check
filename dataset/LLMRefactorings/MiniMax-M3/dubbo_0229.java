public class dubbo_0229 {

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            super.channelInactive(ctx);
            Channel ch = ctx.channel();
            NettyChannel channel = NettyChannel.getOrAddChannel(ch, connectionClient.getUrl(), connectionClient);
            try {
                Attribute<Boolean> goawayAttr = ch.attr(GO_AWAY_KEY);
                if (!Boolean.TRUE.equals(goawayAttr.get())) {
                    reconnect(ch);
                }

                logDisconnection(ch, channel);
            } finally {
                NettyChannel.removeChannel(ch);
            }
        }

        private void logDisconnection(Channel ch, NettyChannel channel) {
            if (LOGGER.isInfoEnabled() && channel != null) {
                LOGGER.info(
                        "The connection {} of {} -> {} is disconnected.",
                        ch,
                        channel.getLocalAddressKey(),
                        channel.getRemoteAddressKey());
            }
        }
}
