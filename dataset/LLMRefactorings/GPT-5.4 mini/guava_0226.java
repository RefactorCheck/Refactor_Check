public class guava_0226 {

        static final Iterator<N> postOrder(Iterator<? extends N> startNodes) {
          Deque<N> ancestorStack = new ArrayDeque<>();
          Deque<Iterator<? extends N>> horizon = new ArrayDeque<>();
          horizon.add(startNodes);
          return new AbstractIterator<N>() {
            @Override
            protected @Nullable N computeNext() {
              for (N next = visitNext(horizon); next != null; next = visitNext(horizon)) {
                Iterator<? extends N> successors = successorFunction.successors(next).iterator();
                if (!successors.hasNext()) {
                  return next;
                }
                horizon.addFirst(successors);
                ancestorStack.push(next);
              }
              // TODO(b/192579700): Use a ternary once it no longer confuses our nullness checker.
              if (!ancestorStack.isEmpty()) {
                return ancestorStack.pop();
              }
              return endOfData();
            }
          };
        }
}
