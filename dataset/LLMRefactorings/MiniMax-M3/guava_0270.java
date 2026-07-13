public class guava_0270 {

        private Iterator<N> topDown(Iterator<? extends N> startNodes, InsertionOrder order) {
          Deque<Iterator<? extends N>> horizon = new ArrayDeque<>();
          horizon.add(startNodes);
          return new AbstractIterator<N>() {
            @Override
            protected @Nullable N computeNext() {
              do {
                N next = visitNext(horizon);
                if (next != null) {
                  addSuccessors(next, horizon, order);
                  return next;
                }
              } while (!horizon.isEmpty());
              return endOfData();
            }

            private void addSuccessors(N node, Deque<Iterator<? extends N>> horizon, InsertionOrder order) {
              Iterator<? extends N> successors = successorFunction.successors(node).iterator();
              if (successors.hasNext()) {
                // BFS: horizon.addLast(successors)
                // Pre-order: horizon.addFirst(successors)
                order.insertInto(horizon, successors);
              }
            }
          };
        }
}
