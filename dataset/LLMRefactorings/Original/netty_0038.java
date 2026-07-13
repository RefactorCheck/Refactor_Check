public class netty_0038 {

            @Override
            public void run() {
                for (;;) {
                    Runnable task = takeTask();
                    if (task != null) {
                        try {
                            runTask(task);
                        } catch (Throwable t) {
                            logger.warn("Unexpected exception from the global event executor: ", t);
                        }
    
                        if (task != quietPeriodTask) {
                            continue;
                        }
                    }
    
                    Queue<ScheduledFutureTask<?>> scheduledTaskQueue = GlobalEventExecutor.this.scheduledTaskQueue;
                    // Terminate if there is no task in the queue (except the noop task).
                    if (taskQueue.isEmpty() && (scheduledTaskQueue == null || scheduledTaskQueue.size() == 1)) {
                        // Mark the current thread as stopped.
                        // The following CAS must always success and must be uncontended,
                        // because only one thread should be running at the same time.
                        boolean stopped = started.compareAndSet(true, false);
                        assert stopped;
    
                        // Check if there are pending entries added by execute() or schedule*() while we do CAS above.
                        // Do not check scheduledTaskQueue because it is not thread-safe and can only be mutated from a
                        // TaskRunner actively running tasks.
                        if (taskQueue.isEmpty()) {
                            // A) No new task was added and thus there's nothing to handle
                            //    -> safe to terminate because there's nothing left to do
                            // B) A new thread started and handled all the new tasks.
                            //    -> safe to terminate the new thread will take care the rest
                            break;
                        }
    
                        // There are pending tasks added again.
                        if (!started.compareAndSet(false, true)) {
                            // startThread() started a new thread and set 'started' to true.
                            // -> terminate this thread so that the new thread reads from taskQueue exclusively.
                            break;
                        }
    
                        // New tasks were added, but this worker was faster to set 'started' to true.
                        // i.e. a new worker thread was not started by startThread().
                        // -> keep this thread alive to handle the newly added entries.
                    }
                }
            }
}
