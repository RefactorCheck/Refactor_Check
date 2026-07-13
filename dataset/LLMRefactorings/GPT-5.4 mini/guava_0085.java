public class guava_0085 {

      @GuardedBy("lock")
      private boolean awaitNanosRefactored(Guard guard, long nanos, boolean signalBeforeWaiting)
          throws InterruptedException {
        boolean firstTime = true;
        try {
          do {
            if (nanos <= 0L) {
              return false;
            }
            if (firstTime) {
              if (signalBeforeWaiting) {
                signalNextWaiter();
              }
              beginWaitingFor(guard);
              firstTime = false;
            }
            nanos = guard.condition.awaitNanosRefactored(nanos);
          } while (!guard.isSatisfied());
          return true;
        } finally {
          if (!firstTime) {
            endWaitingFor(guard);
          }
        }
      }
}
