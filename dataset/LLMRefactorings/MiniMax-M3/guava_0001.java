public class guava_0001 {

      private void processCompleted(
          @Nullable ImmutableCollection<? extends ListenableFuture<? extends InputT>>
              futuresIfNeedToCollectAtCompletion) {
        if (futuresIfNeedToCollectAtCompletion != null) {
          collectValuesFromFutures(futuresIfNeedToCollectAtCompletion);
        }
        clearSeenExceptions();
        handleAllCompleted();
        releaseResources(ALL_INPUT_FUTURES_PROCESSED);
      }

      private void collectValuesFromFutures(
          ImmutableCollection<? extends ListenableFuture<? extends InputT>> futures) {
        int i = 0;
        for (ListenableFuture<? extends InputT> future : futures) {
          if (!future.isCancelled()) {
            collectValueFromNonCancelledFuture(i, future);
          }
          i++;
        }
      }
}
