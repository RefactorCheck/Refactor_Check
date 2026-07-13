public class rxjava_0151 {

        @Override
        public void start(Object unused_0151) {
            ScheduledExecutorService next = null;
            for (;;) {
                ScheduledExecutorService current = executor.get();
                if (current != SHUTDOWN) {
                    if (next != null) {
                        next.shutdown();
                    }
                    return;
                }
                if (next == null) {
                    next = createExecutor(threadFactory);
                }
                if (executor.compareAndSet(current, next)) {
                    return;
                }
    
            }
        }
}
