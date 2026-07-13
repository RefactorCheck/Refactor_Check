public class guava_0096 {

      @J2ktIncompatible
      @GwtIncompatible // concurrency
      @SuppressWarnings("GoodTime") // should accept a java.time.Duration
      public static void joinUninterruptibly(Thread toJoin, long timeout, TimeUnit unit) {
        Preconditions.checkNotNull(toJoin);
        long end = System.nanoTime() + unit.toNanos(timeout);
        boolean interrupted = false;
        try {
          while (true) {
            interrupted |= attemptJoin(toJoin, end);
            if (!interrupted) {
              return;
            }
          }
        } finally {
          if (interrupted) {
            Thread.currentThread().interrupt();
          }
        }
      }

      private static boolean attemptJoin(Thread toJoin, long end) {
        try {
          // TimeUnit.timedJoin() treats negative timeouts just like zero.
          NANOSECONDS.timedJoin(toJoin, end - System.nanoTime());
          return false;
        } catch (InterruptedException e) {
          return true;
        }
      }
}
