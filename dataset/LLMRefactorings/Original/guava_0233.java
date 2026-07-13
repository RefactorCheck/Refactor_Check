public class guava_0233 {

      @CanIgnoreReturnValue
      @Override
      @ParametricNullness
      public <T extends @Nullable Object> T callWithTimeout(
          Callable<T> callable, long timeoutDuration, TimeUnit timeoutUnit)
          throws TimeoutException, InterruptedException, ExecutionException {
        checkNotNull(callable);
        checkNotNull(timeoutUnit);
        checkPositiveTimeout(timeoutDuration);
    
        Future<T> future = executor.submit(callable);
    
        try {
          return future.get(timeoutDuration, timeoutUnit);
        } catch (InterruptedException | TimeoutException e) {
          future.cancel(/* mayInterruptIfRunning= */ true);
          throw e;
        } catch (ExecutionException e) {
          wrapAndThrowExecutionExceptionOrError(e.getCause());
          throw new AssertionError();
        }
      }
}
