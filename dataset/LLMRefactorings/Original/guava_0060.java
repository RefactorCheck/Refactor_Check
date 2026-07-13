public class guava_0060 {

        public ListenableFuture<V> loadFuture(K key, CacheLoader<? super K, V> loader) {
          try {
            stopwatch.start();
            V previousValue = oldValue.get();
            if (previousValue == null) {
              V newValue = loader.load(key);
              return set(newValue) ? futureValue : immediateFuture(newValue);
            }
            ListenableFuture<V> newValue = loader.reload(key, previousValue);
            if (newValue == null) {
              return immediateFuture(null);
            }
            // To avoid a race, make sure the refreshed value is set into loadingValueReference
            // *before* returning newValue from the cache query.
            return transform(
                newValue,
                newResult -> {
                  LoadingValueReference.this.set(newResult);
                  return newResult;
                },
                directExecutor());
          } catch (Throwable t) {
            ListenableFuture<V> result = setException(t) ? futureValue : fullyFailedFuture(t);
            if (t instanceof InterruptedException) {
              Thread.currentThread().interrupt();
            }
            return result;
          }
        }
}
