public class guava_0085 {

      @GuardedBy("lock")
      private boolean awaitNanos(Guard guard, long nanos, boolean signalBeforeWaiting)
          throws InterruptedException {
        boolean firstTime = true;
        try {
          do {
            if (nanos <= 0L) {
              return false;
            }
            if (firstTime) {
              signalAndBegin(guard, signalBeforeWaiting);
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

      private void signalAndBegin(Guard guard, boolean signalBeforeWaiting) {
        if (signalBeforeWaiting) {
          signalNextWaiter();
        }
        beginWaitingFor(guard);
      }
}
