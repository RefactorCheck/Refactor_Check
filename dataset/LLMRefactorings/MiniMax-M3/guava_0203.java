public class guava_0203 {

    @SuppressWarnings("CatchingUnchecked") // sneaky checked exception
    private void workOnQueue() {
        boolean interruptedDuringTask = false;
        boolean hasSetRunning = false;
        try {
            while (true) {
                synchronized (queue) {
                    if (!hasSetRunning) {
                        if (workerRunningState == RUNNING) {
                            return;
                        } else {
                            workerRunCount++;
                            workerRunningState = RUNNING;
                            hasSetRunning = true;
                        }
                    }
                    task = queue.poll();
                    if (task == null) {
                        workerRunningState = IDLE;
                        return;
                    }
                }
                interruptedDuringTask |= Thread.interrupted();
                executeTask();
            }
        } finally {
            if (interruptedDuringTask) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void executeTask() {
        try {
            task.run();
        } catch (Exception e) {
            log.get().log(Level.SEVERE, "Exception while executing runnable " + task, e);
        } finally {
            task = null;
        }
    }
}
