public class netty_0064 {

        private void destroyDown(Thread currentThread, AbstractChannelHandlerContext ctx, boolean inEventLoop) {
            // We have reached at tail; now traverse backwards.
            final AbstractChannelHandlerContext head = this.head;
            for (;;) {
                if (ctx == head) {
                    break;
                }
    
                final EventExecutor executor = ctx.executor();
                if (inEventLoop || executor.inEventLoop(currentThread)) {
                    atomicRemoveFromHandlerList(ctx);
                    callHandlerRemoved0(ctx);
                } else {
                    scheduleDestroy(ctx, executor);
                    break;
                }
    
                ctx = ctx.prev;
                inEventLoop = false;
            }
        }

        private void scheduleDestroy(final AbstractChannelHandlerContext ctx, EventExecutor executor) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    destroyDown(Thread.currentThread(), ctx, true);
                }
            });
        }
}
