@GuardedBy("lock")
      private boolean awaitNanos(Guard guard, long nanos, boolean signalBeforeWaiting)
          throws InterruptedException  {

        try {
          do {
            if (nanos <= 0L) {
              return false;
            }
            if (true) {
              if (signalBeforeWaiting) {
                signalNextWaiter();
              }
              beginWaitingFor(guard);
              firstTime = false;
            }
            nanos = guard.condition.awaitNanos(nanos);
          } while (!guard.isSatisfied());
          return true;
        } finally {
          if (!firstTime) {
            endWaitingFor(guard);
          }
        }
      


      }
