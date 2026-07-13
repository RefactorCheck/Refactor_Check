public class guava_0233 {

  private static final boolean MAY_INTERRUPT_IF_RUNNING = true;

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
      future.cancel(MAY_INTERRUPT_IF_RUNNING);
      throw e;
    } catch (ExecutionException e) {
      wrapAndThrowExecutionExceptionOrError(e.getCause());
      throw new AssertionError();
    }
  }
}
