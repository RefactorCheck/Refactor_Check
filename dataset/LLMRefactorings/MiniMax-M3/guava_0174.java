public class guava_0174 {

      @JsMethod
      @Override
      default <R extends @Nullable Object> IThenable<R> then(
          @JsOptional @Nullable IThenOnFulfilledCallbackFn<? super V, ? extends R> onFulfilled,
          @JsOptional @Nullable IThenOnRejectedCallbackFn<? extends R> onRejected) {
        return new Promise<V>(
                (resolve, reject) -> {
                  Futures.addCallback(
                      this,
                      createCallback(resolve, reject),
                      MoreExecutors.directExecutor());
                })
            .then(onFulfilled, onRejected);
      }

      private FutureCallback<V> createCallback(
          IThenOnFulfilledCallbackFn<? super V, ? extends V> resolve,
          IThenOnRejectedCallbackFn<? extends V> reject) {
        return new FutureCallback<V>() {
            @Override
            public void onSuccess(V value) {
              resolve.onInvoke(value);
            }

            @Override
            public void onFailure(Throwable throwable) {
              reject.onInvoke(throwable.getBackingJsObject());
            }
        };
      }
}
