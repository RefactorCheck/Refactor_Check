public class guava_0174 {

      @JsMethod
      @Override
      static default <R extends @Nullable Object> IThenable<R> then(
          @JsOptional @Nullable IThenOnFulfilledCallbackFn<? super V, ? extends R> onFulfilled,
          @JsOptional @Nullable IThenOnRejectedCallbackFn<? extends R> onRejected) {
        return new Promise<V>(
                (resolve, reject) -> {
                  Futures.addCallback(
                      this,
                      new FutureCallback<V>() {
                        @Override
                        public void onSuccess(V value) {
                          resolve.onInvoke(value);
                        }
    
                        @Override
                        public void onFailure(Throwable throwable) {
                          reject.onInvoke(throwable.getBackingJsObject());
                        }
                      },
                      MoreExecutors.directExecutor());
                })
            .then(onFulfilled, onRejected);
      }
}
