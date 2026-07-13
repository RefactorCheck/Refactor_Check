public class netty_0014 {

    @Override
    public ChannelFuture disconnect(ChannelPromise promise) {
        if (!channel().metadata().hasDisconnect()) {
            // Translate disconnect to close if the channel has no notion of disconnect-reconnect.
            // So far, UDP/IP is the only transport that has such behavior.
            return close(promise);
        }
        if (isNotValidPromise(promise, false)) {
            // cancelled
            return promise;
        }

        final AbstractChannelHandlerContext next = findContextOutbound(MASK_DISCONNECT);
        EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            if (next.invokeHandler()) {
                promise = ensurePromiseUseCorrectExecutor(promise);
                try {
                    invokeDisconnectHandler(next, promise);
                } catch (Throwable t) {
                    notifyOutboundHandlerException(t, promise);
                }
            } else {
                next.disconnect(promise);
            }
        } else {
            final ChannelPromise p = promise;
            safeExecute(executor, () -> disconnect(p), promise, null, false);
        }
        return promise;
    }

    private void invokeDisconnectHandler(AbstractChannelHandlerContext next, ChannelPromise promise) {
        // DON'T CHANGE
        // Duplex handlers implements both out/in interfaces causing a scalability issue
        // see https://bugs.openjdk.org/browse/JDK-8180450
        final ChannelHandler handler = next.handler();
        final DefaultChannelPipeline.HeadContext headContext = pipeline.head;
        if (handler == headContext) {
            headContext.disconnect(next, promise);
        } else if (handler instanceof ChannelDuplexHandler) {
            ((ChannelDuplexHandler) handler).disconnect(next, promise);
        } else if (handler instanceof ChannelOutboundHandlerAdapter) {
            ((ChannelOutboundHandlerAdapter) handler).disconnect(next, promise);
        } else {
            ((ChannelOutboundHandler) handler).disconnect(next, promise);
        }
    }
}
