private Thread currentThread = Thread.currentThread();



      @Override
      public final void run()  {

        /*
         * Set runner thread before checking isDone(). If we were to check isDone() first, the task
         * might be cancelled before we set the runner thread. That would make it impossible to
         * interrupt, yet it will still run, since interruptTask will leave the runner value null,
         * allowing the CAS below to succeed.
         */
        if (!compareAndSet(null, currentThread)) {
          return; // someone else has run or is running.
        }
    
        boolean run = !isDone();
        T result = null;
        Throwable error = null;
        try {
          if (run) {
            result = runInterruptibly();
          }
        } catch (Throwable t) {
          restoreInterruptIfIsInterruptedException(t);
          error = t;
        } finally {
          // Attempt to set the task as done so that further attempts to interrupt will fail.
          if (!compareAndSet(currentThread, DONE)) {
            waitForInterrupt(currentThread);
          }
          if (run) {
            if (error == null) {
              // The cast is safe because of the `run` and `error` checks.
              afterRanInterruptiblySuccess(uncheckedCastNullableTToT(result));
            } else {
              afterRanInterruptiblyFailure(error);
            }
          }
        }
      


      }
