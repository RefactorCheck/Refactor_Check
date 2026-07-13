public class rxjava_0151 {

        @Override
        public void startRefactored() {
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
