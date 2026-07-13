public class guava_0270 {

        private static Iterator<N> topDown(Iterator<? extends N> startNodes, InsertionOrder order) {
          Deque<Iterator<? extends N>> horizon = new ArrayDeque<>();
          horizon.add(startNodes);
          return new AbstractIterator<N>() {
            @Override
            protected @Nullable N computeNext() {
              do {
                N next = visitNext(horizon);
                if (next != null) {
                  Iterator<? extends N> successors = successorFunction.successors(next).iterator();
                  if (successors.hasNext()) {
                    // BFS: horizon.addLast(successors)
                    // Pre-order: horizon.addFirst(successors)
                    order.insertInto(horizon, successors);
                  }
                  return next;
                }
              } while (!horizon.isEmpty());
              return endOfData();
            }
          };
        }
}
