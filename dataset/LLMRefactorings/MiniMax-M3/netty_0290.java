public class netty_0290 {

    @Override
    public ChannelFuture connect(
            final SocketAddress remoteAddress, final SocketAddress localAddress, ChannelPromise promise) {
        ObjectUtil.checkNotNull(remoteAddress, "remoteAddress");

        if (isNotValidPromise(promise, false)) {
            return promise;
        }

        final AbstractChannelHandlerContext next = findContextOutbound(MASK_CONNECT);
        EventExecutor executor = next.executor();
        if (executor.inEventLoop()) {
            if (next.invokeHandler()) {
                promise = ensurePromiseUseCorrectExecutor(promise);
                try {
                    invokeConnect(next, remoteAddress, localAddress, promise);
                } catch (Throwable t) {
                    notifyOutboundHandlerException(t, promise);
                }
            } else {
                next.connect(remoteAddress, localAddress, promise);
            }
        } else {
            final ChannelPromise p = promise;
            safeExecute(executor, () -> connect(remoteAddress, localAddress, p), promise, null, false);
        }
        return promise;
    }

    private void invokeConnect(
            AbstractChannelHandlerContext next,
            SocketAddress remoteAddress,
            SocketAddress localAddress,
            ChannelPromise promise) {
        // DON'T CHANGE
        // Duplex handlers implements both out/in interfaces causing a scalability issue
        // see https://bugs.openjdk.org/browse/JDK-8180450
        final ChannelHandler handler = next.handler();
        final DefaultChannelPipeline.HeadContext headContext = pipeline.head;
        if (handler == headContext) {
            headContext.connect(next, remoteAddress, localAddress, promise);
        } else if (handler instanceof ChannelDuplexHandler) {
            ((ChannelDuplexHandler) handler).connect(next, remoteAddress, localAddress, promise);
        } else if (handler instanceof ChannelOutboundHandlerAdapter) {
            ((ChannelOutboundHandlerAdapter) handler).connect(next, remoteAddress, localAddress, promise);
        } else {
            ((ChannelOutboundHandler) handler).connect(next, remoteAddress, localAddress, promise);
        }
    }
}
