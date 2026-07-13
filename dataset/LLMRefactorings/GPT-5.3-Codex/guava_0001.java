public class guava_0001 {

  private void processCompleted(
      @Nullable ImmutableCollection<? extends ListenableFuture<? extends InputT>>
          futuresIfNeedToCollectAtCompletion) {
    if (futuresIfNeedToCollectAtCompletion != null) {
      int i = 0;
      for (ListenableFuture<? extends InputT> future : futuresIfNeedToCollectAtCompletion) {
        processFuture(i, future);
        i++;
      }
    }
    clearSeenExceptions();
    handleAllCompleted();
    releaseResources(ALL_INPUT_FUTURES_PROCESSED);
  }

  private void processFuture(int index, ListenableFuture<? extends InputT> future) {
    if (!future.isCancelled()) {
      collectValueFromNonCancelledFuture(index, future);
    }
  }
}
