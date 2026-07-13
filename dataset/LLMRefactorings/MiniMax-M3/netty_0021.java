public class netty_0021 {

            @Override
            public final void write(Object msg, ChannelPromise promise) {
                assertEventLoop();
    
                ChannelOutboundBuffer outboundBuffer = this.outboundBuffer;
                if (outboundBuffer == null) {
                    failOnClosedChannel(msg, promise);
                    return;
                }
    
                int size;
                try {
                    msg = filterOutboundMessage(msg);
                    size = pipeline.estimatorHandle().size(msg);
                    if (size < 0) {
                        size = 0;
                    }
                } catch (Throwable t) {
                    try {
                        ReferenceCountUtil.release(msg);
                    } finally {
                        safeSetFailure(promise, t);
                    }
                    return;
                }
    
                outboundBuffer.addMessage(msg, size, promise);
            }
    
            private void failOnClosedChannel(Object msg, ChannelPromise promise) {
                try {
                    ReferenceCountUtil.release(msg);
                } finally {
                    safeSetFailure(promise,
                            newClosedChannelException(initialCloseCause, "write(Object, ChannelPromise)"));
                }
            }
}
