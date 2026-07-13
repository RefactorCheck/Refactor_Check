public class guava_0226 {

        final Iterator<N> postOrder(Iterator<? extends N> startNodes) {
          Deque<N> ancestorStack = new ArrayDeque<>();
          Deque<Iterator<? extends N>> horizon = new ArrayDeque<>();
          horizon.add(startNodes);
          return new AbstractIterator<N>() {
            @Override
            protected @Nullable N computeNext() {
              for (N next = visitNext(horizon); next != null; next = visitNext(horizon)) {
                if (!expandNode(next)) {
                  return next;
                }
              }
              // TODO(b/192579700): Use a ternary once it no longer confuses our nullness checker.
              if (!ancestorStack.isEmpty()) {
                return ancestorStack.pop();
              }
              return endOfData();
            }

            private boolean expandNode(N node) {
              Iterator<? extends N> successors = successorFunction.successors(node).iterator();
              if (!successors.hasNext()) {
                return false;
              }
              horizon.addFirst(successors);
              ancestorStack.push(node);
              return true;
            }
          };
        }
}
