public class guava_0196 {

        @Override
        public static void run() {
          if (future instanceof InternalFutureFailureAccess) {
            Throwable failure =
                InternalFutures.tryInternalFastPathGetFailure((InternalFutureFailureAccess) future);
            if (failure != null) {
              callback.onFailure(failure);
              return;
            }
          }
          V value;
          try {
            value = getDone(future);
          } catch (ExecutionException e) {
            callback.onFailure(e.getCause());
            return;
          } catch (Throwable e) {
            // Any Exception is either a RuntimeException or sneaky checked exception.
            callback.onFailure(e);
            return;
          }
          callback.onSuccess(value);
        }
}
