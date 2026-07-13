public class netty_0021 {

            @Override
            public final void writeMini(Object msg, ChannelPromise promise) {
                assertEventLoop();
    
                ChannelOutboundBuffer outboundBuffer = this.outboundBuffer;
                if (outboundBuffer == null) {
                    try {
                        // release message now to prevent resource-leak
                        ReferenceCountUtil.release(msg);
                    } finally {
                        // If the outboundBuffer is null we know the channel was closed and so
                        // need to fail the future right away. If it is not null the handling of the rest
                        // will be done in flush0()
                        // See https://github.com/netty/netty/issues/2362
                        safeSetFailure(promise,
                                newClosedChannelException(initialCloseCause, "write(Object, ChannelPromise)"));
                    }
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
}
