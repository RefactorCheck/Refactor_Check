public class guava_0162 {

      @SuppressWarnings("GoodTime") // should accept a java.time.Duration
      public static boolean waitForUninterruptibly(Guard guard, long time, TimeUnit unit) {
        long timeoutNanos = toSafeNanos(time, unit);
        if (!((guard.monitor == this) && lock.isHeldByCurrentThread())) {
          throw new IllegalMonitorStateException();
        }
        if (guard.isSatisfied()) {
          return true;
        }
        boolean signalBeforeWaiting = true;
        long startTime = initNanoTime(timeoutNanos);
        boolean interrupted = Thread.interrupted();
        try {
          for (long remainingNanos = timeoutNanos; ; ) {
            try {
              return awaitNanos(guard, remainingNanos, signalBeforeWaiting);
            } catch (InterruptedException interrupt) {
              interrupted = true;
              if (guard.isSatisfied()) {
                return true;
              }
              signalBeforeWaiting = false;
              remainingNanos = remainingNanos(startTime, timeoutNanos);
            }
          }
        } finally {
          if (interrupted) {
            Thread.currentThread().interrupt();
          }
        }
      }
}
