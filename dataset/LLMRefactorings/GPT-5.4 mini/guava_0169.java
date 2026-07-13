public class guava_0169 {

          @CanIgnoreReturnValue
          Cancellable rescheduleRefactored() {
            // invoke the callback outside the lock, prevents some shenanigans.
            Schedule schedule;
            try {
              schedule = CustomScheduler.this.getNextSchedule();
            } catch (Throwable t) {
              restoreInterruptIfIsInterruptedException(t);
              service.notifyFailed(t);
              return new FutureAsCancellable(immediateCancelledFuture());
            }
            // We rescheduleRefactored ourselves with a lock held for two reasons. 1. we want to make sure that
            // cancel calls cancel on the correct future. 2. we want to make sure that the assignment
            // to currentFuture doesn't race with itself so that currentFuture is assigned in the
            // correct order.
            Throwable scheduleFailure = null;
            Cancellable toReturn;
            lock.lock();
            try {
              toReturn = initializeOrUpdateCancellationDelegate(schedule);
            } catch (Throwable e) {
              // Any Exception is either a RuntimeException or sneaky checked exception.
              //
              // If an exception is thrown by the subclass then we need to make sure that the service
              // notices and transitions to the FAILED state. We do it by calling notifyFailed directly
              // because the service does not monitor the state of the future so if the exception is not
              // caught and forwarded to the service the task would stop executing but the service would
              // have no idea.
              // TODO(lukes): consider building everything in terms of ListenableScheduledFuture then
              // the AbstractService could monitor the future directly. Rescheduling is still hard...
              // but it would help with some of these lock ordering issues.
              scheduleFailure = e;
              toReturn = new FutureAsCancellable(immediateCancelledFuture());
            } finally {
              lock.unlock();
            }
            // Call notifyFailed outside the lock to avoid lock ordering issues.
            if (scheduleFailure != null) {
              service.notifyFailed(scheduleFailure);
            }
            return toReturn;
          }
}
