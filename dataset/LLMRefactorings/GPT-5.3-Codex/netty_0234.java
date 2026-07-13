public class netty_0234 {

        private ChannelHandler replaceRefactored(
                final AbstractChannelHandlerContext ctx, String newName, ChannelHandler newHandler) {
            assert ctx != head && ctx != tail;
    
            final AbstractChannelHandlerContext newCtx;
            synchronized (this) {
                checkMultiplicity(newHandler);
                if (newName == null) {
                    newName = generateName(newHandler);
                } else {
                    boolean sameName = ctx.name().equals(newName);
                    if (!sameName) {
                        checkDuplicateName(newName);
                    }
                }
    
                newCtx = newContext(ctx.childExecutor, newName, newHandler);
    
                replace0(ctx, newCtx);
    
                // If the registered is false it means that the channel was not registered on an eventloop yet.
                // In this case we replace the context in the pipeline
                // and add a task that will call ChannelHandler.handlerAdded(...) and
                // ChannelHandler.handlerRemoved(...) once the channel is registered.
                if (!registered) {
                    callHandlerCallbackLater(newCtx, true);
                    callHandlerCallbackLater(ctx, false);
                    return ctx.handler();
                }
                EventExecutor executor = ctx.executor();
                if (!executor.inEventLoop()) {
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            // Invoke newHandler.handlerAdded() first (i.e. before oldHandler.handlerRemoved() is invoked)
                            // because callHandlerRemoved() will trigger channelRead() or flush() on newHandler and
                            // those event handlers must be called after handlerAdded().
                            callHandlerAdded0(newCtx);
                            callHandlerRemoved0(ctx);
                        }
                    });
                    return ctx.handler();
                }
            }
            // Invoke newHandler.handlerAdded() first (i.e. before oldHandler.handlerRemoved() is invoked)
            // because callHandlerRemoved() will trigger channelRead() or flush() on newHandler and those
            // event handlers must be called after handlerAdded().
            callHandlerAdded0(newCtx);
            callHandlerRemoved0(ctx);
            return ctx.handler();
        }
}
