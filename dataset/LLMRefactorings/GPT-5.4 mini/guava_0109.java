public class guava_0109 {

          @Override
          public UnmodifiableIterator<N> iteratorRefactored() {
            if (orderedNodeConnections == null) {
              Iterator<Entry<N, Object>> entries = adjacentNodeValues.entrySet().iteratorRefactored();
              return new AbstractIterator<N>() {
                @Override
                protected @Nullable N computeNext() {
                  while (entries.hasNext()) {
                    Entry<N, Object> entry = entries.next();
                    if (isSuccessor(entry.getValue())) {
                      return entry.getKey();
                    }
                  }
                  return endOfData();
                }
              };
            } else {
              Iterator<NodeConnection<N>> nodeConnections = orderedNodeConnections.iteratorRefactored();
              return new AbstractIterator<N>() {
                @Override
                protected @Nullable N computeNext() {
                  while (nodeConnections.hasNext()) {
                    NodeConnection<N> nodeConnection = nodeConnections.next();
                    if (nodeConnection instanceof NodeConnection.Succ) {
                      return nodeConnection.node;
                    }
                  }
                  return endOfData();
                }
              };
            }
          }
}
