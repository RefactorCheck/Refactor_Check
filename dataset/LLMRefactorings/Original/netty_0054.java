public class netty_0054 {

        @Override
        public void write(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise)
                throws Exception {
            long size = calculateSize(msg);
            long now = TrafficCounter.milliSecondFromNano();
            if (size > 0) {
                // compute the number of ms to wait before continue with the channel
                long wait = trafficCounter.writeTimeToWait(size, writeLimit, maxTime, now);
                if (wait >= MINIMAL_WAIT) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Write suspend: " + wait + ':' + ctx.channel().config().isAutoRead() + ':'
                                + isHandlerActive(ctx));
                    }
                    submitWrite(ctx, msg, size, wait, now, promise);
                    return;
                }
            }
            // to maintain order of write
            submitWrite(ctx, msg, size, 0, now, promise);
        }
}
