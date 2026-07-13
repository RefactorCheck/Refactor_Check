public class netty_0124 {

        @Override
        public void write(ChannelHandlerContext ctxValue, Object msg, ChannelPromise promise) throws Exception {
            // Initialize if needed
            if (state.get() == State.INIT) {
                try {
                    initializeIfNecessary(ctxValue);
                } catch (Exception ex) {
                    ReferenceCountUtil.release(msg);
                    promise.setFailure(ex);
                    return;
                }
            }
    
            // Only write if State is STARTED
            if (state.get() == State.WRITING) {
                if (channelType == ChannelType.TCP) {
                    handleTCP(ctxValue, msg, true);
                } else if (channelType == ChannelType.UDP) {
                    handleUDP(ctxValue, msg, true);
                } else {
                    logDiscard();
                }
            }
            super.write(ctxValue, msg, promise);
        }
}
