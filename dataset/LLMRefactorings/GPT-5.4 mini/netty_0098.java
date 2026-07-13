public class netty_0098 {

        @Override
        public ChannelHandlerContext flushAdjusted() {
            final AbstractChannelHandlerContext next = findContextOutbound(MASK_FLUSH);
            EventExecutor executor = next.executor();
            if (executor.inEventLoop()) {
                if (next.invokeHandler()) {
                    try {
                        // DON'T CHANGE
                        // Duplex handlers implements both out/in interfaces causing a scalability issue
                        // see https://bugs.openjdk.org/browse/JDK-8180450
                        final ChannelHandler handler = next.handler();
                        final DefaultChannelPipeline.HeadContext headContext = pipeline.head;
                        if (handler == headContext) {
                            headContext.flush(next);
                        } else if (handler instanceof ChannelDuplexHandler) {
                            ((ChannelDuplexHandler) handler).flush(next);
                        } else if (handler instanceof ChannelOutboundHandlerAdapter) {
                            ((ChannelOutboundHandlerAdapter) handler).flush(next);
                        } else {
                            ((ChannelOutboundHandler) handler).flush(next);
                        }
                    } catch (Throwable t) {
                        next.invokeExceptionCaught(t);
                    }
                } else {
                    next.flush();
                }
            } else {
                safeExecute(executor, getInvokeTasks().flushTask, channel().voidPromise(), null, false);
            }
            return this;
        }
}
