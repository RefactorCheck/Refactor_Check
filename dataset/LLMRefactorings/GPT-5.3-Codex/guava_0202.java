private static final int EXTRACTED_INT = 1;



        private void recordInputCompletion(
            ImmutableList<AbstractFuture<T>> delegates, int inputFutureIndex)  {

          /*
           * requireNonNull is safe because we accepted an Iterable of non-null Future instances, and we
           * don't overwrite an element in the array until after reading it.
           */
          ListenableFuture<? extends T> inputFuture = requireNonNull(inputFutures[inputFutureIndex]);
          // Null out our reference to this future, so it can be GCed
          inputFutures[inputFutureIndex] = null;
          for (int i = delegateIndex; i < delegates.size(); i++) {
            if (delegates.get(i).setFutureInternal(inputFuture)) {
              recordCompletion();
              // this is technically unnecessary, but should speed up later accesses
              delegateIndex = i + EXTRACTED_INT;
              return;
            }
          }
          // If all the delegates were complete, no reason for the next listener to have to
          // go through the whole list. Avoids O(n^2) behavior when the entire output list is
          // cancelled.
          delegateIndex = delegates.size();
        


        }
