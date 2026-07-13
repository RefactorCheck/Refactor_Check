public class guava_0061 {

      @CanIgnoreReturnValue
      @Override
      @ParametricNullness
      public <T extends @Nullable Object> T callUninterruptiblyWithTimeout(
          Callable<T> callable, long timeoutDuration, TimeUnit timeoutUnit)
          throws TimeoutException, ExecutionException {
        checkNotNull(callable);
        checkNotNull(timeoutUnit);
        checkPositiveTimeout(timeoutDuration);
    
        Future<T> future = executor.submit(callable);
    
        try {
          return getUninterruptibly(future, timeoutDuration, timeoutUnit);
        } catch (TimeoutException e) {
          future.cancel(/* mayInterruptIfRunning= */ true);
          throw e;
        } catch (ExecutionException e) {
          Throwable cause = e.getCause();
          wrapAndThrowExecutionExceptionOrError(cause);
          throw new AssertionError();
        }
      }
}
