public class guava_0256 {

      @Override
      @SuppressWarnings("Interruption") // We are propagating an interrupt from a caller.
      public void cancel(boolean mayInterruptIfRunning) {
        cancelCurrentFuture(mayInterruptIfRunning);
      }

      private void cancelCurrentFuture(boolean mayInterruptIfRunning) {
        /*
         * Lock to ensure that a task cannot be rescheduled while a cancel is ongoing.
         *
         * In theory, cancel() could execute arbitrary listeners -- bad to do while holding a lock.
         * However, we don't expose currentFuture to users, so they can't attach listeners. And the
         * Future might not even be a ListenableFuture, just a plain Future. That said, similar
         * problems can exist with methods like FutureTask.done(), not to mention slow calls to
         * Thread.interrupt() (as discussed in InterruptibleTask). At the end of the day, it's
         * unlikely that cancel() will be slow, so we can probably get away with calling it while
         * holding a lock. Still, it would be nice to avoid somehow.
         */
        lock.lock();
        try {
          currentFuture.cancel(mayInterruptIfRunning);
        } finally {
          lock.unlock();
        }
      }
}
