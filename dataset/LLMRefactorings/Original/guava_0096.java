public class guava_0096 {

      @J2ktIncompatible
      @GwtIncompatible // concurrency
      @SuppressWarnings("GoodTime") // should accept a java.time.Duration
      public static void joinUninterruptibly(Thread toJoin, long timeout, TimeUnit unit) {
        Preconditions.checkNotNull(toJoin);
        boolean interrupted = false;
        try {
          long remainingNanos = unit.toNanos(timeout);
          long end = System.nanoTime() + remainingNanos;
          while (true) {
            try {
              // TimeUnit.timedJoin() treats negative timeouts just like zero.
              NANOSECONDS.timedJoin(toJoin, remainingNanos);
              return;
            } catch (InterruptedException e) {
              interrupted = true;
              remainingNanos = end - System.nanoTime();
            }
          }
        } finally {
          if (interrupted) {
            Thread.currentThread().interrupt();
          }
        }
      }
}
