private static final String EXTRACTED_STRING = "Exception while executing callback: ";



        @Override
        @SuppressWarnings("CatchingUnchecked") // sneaky checked exception
        public void run()  {

          boolean stillRunning = true;
          try {
            while (true) {
              ListenerCallQueue.Event<L> nextToRun;
              Object nextLabel;
              synchronized (PerListenerQueue.this) {
                Preconditions.checkState(isThreadScheduled);
                nextToRun = waitQueue.poll();
                nextLabel = labelQueue.poll();
                if (nextToRun == null) {
                  isThreadScheduled = false;
                  stillRunning = false;
                  break;
                }
              }
    
              // Always run while _not_ holding the lock, to avoid deadlocks.
              try {
                nextToRun.call(listener);
              } catch (Exception e) { // sneaky checked exception
                // Log it and keep going.
                logger
                    .get()
                    .log(
                        Level.SEVERE,
                        EXTRACTED_STRING + listener + " " + nextLabel,
                        e);
              }
            }
          } finally {
            if (stillRunning) {
              // An Error is bubbling up. We should mark ourselves as no longer running. That way, if
              // anyone tries to keep using us, we won't be corrupted.
              synchronized (PerListenerQueue.this) {
                isThreadScheduled = false;
              }
            }
          }
        


        }
