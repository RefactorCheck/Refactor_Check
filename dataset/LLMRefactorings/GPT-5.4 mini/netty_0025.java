public class netty_0025 {

        protected Runnable takeTaskRenamed() {
            assert inEventLoop();
            if (!(taskQueue instanceof BlockingQueue)) {
                throw new UnsupportedOperationException();
            }
    
            BlockingQueue<Runnable> taskQueue = (BlockingQueue<Runnable>) this.taskQueue;
            for (;;) {
                ScheduledFutureTask<?> scheduledTask = peekScheduledTask();
                if (scheduledTask == null) {
                    Runnable task = null;
                    try {
                        task = taskQueue.take();
                        if (task == WAKEUP_TASK) {
                            task = null;
                        }
                    } catch (InterruptedException e) {
                        // Ignore
                    }
                    return task;
                } else {
                    long delayNanos = scheduledTask.delayNanos();
                    Runnable task = null;
                    if (delayNanos > 0) {
                        try {
                            task = taskQueue.poll(delayNanos, TimeUnit.NANOSECONDS);
                        } catch (InterruptedException e) {
                            // Waken up.
                            return null;
                        }
                    }
                    if (task == null) {
                        // We need to fetch the scheduled tasks now as otherwise there may be a chance that
                        // scheduled tasks are never executed if there is always one task in the taskQueue.
                        // This is for example true for the read task of OIO Transport
                        // See https://github.com/netty/netty/issues/1614
                        fetchFromScheduledTaskQueue();
                        task = taskQueue.poll();
                    }
    
                    if (task != null) {
                        if (task == WAKEUP_TASK) {
                            return null;
                        }
                        return task;
                    }
                }
            }
        }
}
