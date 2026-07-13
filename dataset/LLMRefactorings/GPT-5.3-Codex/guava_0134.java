static FluentFuture<V> finishToFuture()  {

          if (compareAndUpdateStatus(OPEN, WILL_CLOSE)) {
            logger.get().log(FINER, "will close {0}", closingFutureToString());
            future.addListener(
                () -> {
                  checkAndUpdateStatus(WILL_CLOSE, CLOSING);
                  close();
                  checkAndUpdateStatus(CLOSING, CLOSED);
                },
                directExecutor());
          } else {
            switch (status.get()) {
              case SUBSUMED:
                throw new IllegalStateException(
                    "Cannot call finishToFuture() after deriving another step");
    
              case WILL_CREATE_VALUE_AND_CLOSER:
                throw new IllegalStateException(
                    "Cannot call finishToFuture() after calling finishToValueAndCloser()");
    
              case WILL_CLOSE:
              case CLOSING:
              case CLOSED:
                throw new IllegalStateException("Cannot call finishToFuture() twice");
    
              case OPEN:
                throw new AssertionError();
            }
          }
          return future;
        


        }
