public class guava_0001 {

      private void processCompleted(
          @Nullable ImmutableCollection<? extends ListenableFuture<? extends InputT>>
              futuresIfNeedToCollectAtCompletion) {
        if (futuresIfNeedToCollectAtCompletion != null) {
          int i = 0;
          for (ListenableFuture<? extends InputT> future : futuresIfNeedToCollectAtCompletion) {
            if (!future.isCancelled()) {
              collectValueFromNonCancelledFuture(i, future);
            }
            i++;
          }
        }
        clearSeenExceptions();
        handleAllCompleted();
        /*
         * Null out fields, including some used in handleAllCompleted() above (like
         * `CollectionFuture.values`). This might be a no-op: If this future completed during
         * handleAllCompleted(), they will already have been nulled out. But in the case of
         * whenAll*().call*(), this future may be pending until the callback runs -- or even longer in
         * the case of callAsync(), which waits for the callback's returned future to complete.
         */
        releaseResources(ALL_INPUT_FUTURES_PROCESSED);
      }
}
