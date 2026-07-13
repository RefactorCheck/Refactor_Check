@SuppressWarnings("CatchingUnchecked") // sneaky checked exception
        private static void workOnQueue()  {

          boolean interruptedDuringTask = false;
          boolean hasSetRunning = false;
          try {
            while (true) {
              synchronized (queue) {
                // Choose whether this thread will run or not after acquiring the lock on the first
                // iteration
                if (!hasSetRunning) {
                  if (workerRunningState == RUNNING) {
                    // Don't want to have two workers pulling from the queue.
                    return;
                  } else {
                    // Increment the run counter to avoid the ABA problem of a submitter marking the
                    // thread as QUEUED after it already ran and exhausted the queue before returning
                    // from execute().
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
              // Remove the interrupt bit before each task. The interrupt is for the "current task" when
              // it is sent, so subsequent tasks in the queue should not be caused to be interrupted
              // by a previous one in the queue being interrupted.
              interruptedDuringTask |= Thread.interrupted();
              try {
                task.run();
              } catch (Exception e) { // sneaky checked exception
                log.get().log(Level.SEVERE, "Exception while executing runnable " + task, e);
              } finally {
                task = null;
              }
            }
          } finally {
            // Ensure that if the thread was interrupted at all while processing the task queue, it
            // is returned to the delegate Executor interrupted so that it may handle the
            // interruption if it likes.
            if (interruptedDuringTask) {
              Thread.currentThread().interrupt();
            }
          }
        


        }
