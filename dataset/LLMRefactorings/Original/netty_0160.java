public class netty_0160 {

        @Override
        public ChannelFuture close(ChannelPromise promise) {
            if (isNotValidPromise(promise, false)) {
                // cancelled
                return promise;
            }
    
            final AbstractChannelHandlerContext next = findContextOutbound(MASK_CLOSE);
            EventExecutor executor = next.executor();
            if (executor.inEventLoop()) {
                if (next.invokeHandler()) {
                    promise = ensurePromiseUseCorrectExecutor(promise);
                    try {
                        // DON'T CHANGE
                        // Duplex handlers implements both out/in interfaces causing a scalability issue
                        // see https://bugs.openjdk.org/browse/JDK-8180450
                        final ChannelHandler handler = next.handler();
                        final DefaultChannelPipeline.HeadContext headContext = pipeline.head;
                        if (handler == headContext) {
                            headContext.close(next, promise);
                        } else if (handler instanceof ChannelDuplexHandler) {
                            ((ChannelDuplexHandler) handler).close(next, promise);
                        } else if (handler instanceof ChannelOutboundHandlerAdapter) {
                            ((ChannelOutboundHandlerAdapter) handler).close(next, promise);
                        } else {
                            ((ChannelOutboundHandler) handler).close(next, promise);
                        }
                    } catch (Throwable t) {
                        notifyOutboundHandlerException(t, promise);
                    }
                } else {
                    next.close(promise);
                }
            } else {
                final ChannelPromise p = promise;
                safeExecute(executor, () -> close(p), promise, null, false);
            }
    
            return promise;
        }
}
