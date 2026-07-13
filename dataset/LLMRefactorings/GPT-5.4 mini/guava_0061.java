public class guava_0061 {

      @CanIgnoreReturnValue
      @Override
      @ParametricNullness
      public <T extends @Nullable Object> T callUninterruptiblyWithTimeoutRefactored(
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
          wrapAndThrowExecutionExceptionOrError(e.getCause());
          throw new AssertionError();
        }
      }
}
