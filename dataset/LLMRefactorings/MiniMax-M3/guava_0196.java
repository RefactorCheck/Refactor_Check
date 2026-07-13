public class guava_0196 {

        @Override
        public void run() {
          if (future instanceof InternalFutureFailureAccess) {
            Throwable failure =
                InternalFutures.tryInternalFastPathGetFailure((InternalFutureFailureAccess) future);
            if (failure != null) {
              callback.onFailure(failure);
              return;
            }
          }
          try {
            callback.onSuccess(getValueOrThrow(future));
          } catch (Throwable e) {
            callback.onFailure(e);
          }
        }

        private V getValueOrThrow(java.util.concurrent.Future<V> future) throws Throwable {
          try {
            return getDone(future);
          } catch (ExecutionException e) {
            throw e.getCause();
          }
        }
}
