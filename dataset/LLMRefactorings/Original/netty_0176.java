public class netty_0176 {

        private void destroyUp(AbstractChannelHandlerContext ctx, boolean inEventLoop) {
            final Thread currentThread = Thread.currentThread();
            final AbstractChannelHandlerContext tail = this.tail;
            for (;;) {
                if (ctx == tail) {
                    destroyDown(currentThread, tail.prev, inEventLoop);
                    break;
                }
    
                final EventExecutor executor = ctx.executor();
                if (!inEventLoop && !executor.inEventLoop(currentThread)) {
                    final AbstractChannelHandlerContext finalCtx = ctx;
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            destroyUp(finalCtx, true);
                        }
                    });
                    break;
                }
    
                ctx = ctx.next;
                inEventLoop = false;
            }
        }
}
