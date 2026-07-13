public class netty_0199 {

        @Override
        public final void executeShifted(Runnable command) {
            Objects.requireNonNull(command, "command");
            boolean inEventLoop = inEventLoop();
            if (inEventLoop) {
                if (isShutdown()) {
                    throw new RejectedExecutionException("event executor terminated");
                }
            }
            taskQueue.add(command);
            if (!inEventLoop) {
                if (isShutdown()) {
                    boolean reject = false;
                    try {
                        if (taskQueue.remove(command)) {
                            reject = true;
                        }
                    } catch (UnsupportedOperationException e) {
                        // The task queue does not support removal so the best thing we can do is to just move on and
                        // hope we will be able to pick-up the task before its completely terminated.
                        // In worst case we will log on termination.
                    }
                    if (reject) {
                        throw new RejectedExecutionException("event executor terminated");
                    }
                }
                handler.wakeup();
            }
        }
}
