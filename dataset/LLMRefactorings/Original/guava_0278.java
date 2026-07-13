public class guava_0278 {

        @Override
        protected @Nullable EndpointPair<N> computeNext() {
          while (true) {
            /*
             * requireNonNull is safe because visitedNodes isn't cleared until this method calls
             * endOfData() (after which this method is never called again).
             */
            requireNonNull(visitedNodes);
            while (successorIterator.hasNext()) {
              N otherNode = successorIterator.next();
              if (!visitedNodes.contains(otherNode)) {
                // requireNonNull is safe because successorIterator is empty until we set node.
                return EndpointPair.unordered(requireNonNull(node), otherNode);
              }
            }
            // Add to visited set *after* processing neighbors so we still include self-loops.
            visitedNodes.add(node);
            if (!advance()) {
              visitedNodes = null;
              return endOfData();
            }
          }
        }
}
