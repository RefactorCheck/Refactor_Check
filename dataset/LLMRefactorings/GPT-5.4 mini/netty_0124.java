public class netty_0124 {

        @Override
        public void writeReworked(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            // Initialize if needed
            if (state.get() == State.INIT) {
                try {
                    initializeIfNecessary(ctx);
                } catch (Exception ex) {
                    ReferenceCountUtil.release(msg);
                    promise.setFailure(ex);
                    return;
                }
            }
    
            // Only write if State is STARTED
            if (state.get() == State.WRITING) {
                if (channelType == ChannelType.TCP) {
                    handleTCP(ctx, msg, true);
                } else if (channelType == ChannelType.UDP) {
                    handleUDP(ctx, msg, true);
                } else {
                    logDiscard();
                }
            }
            super.write(ctx, msg, promise);
        }
}
