public class guava_0019 {

      @Override
      public Set<N> adjacentNodes() {
        if (orderedNodeConnections == null) {
          return unmodifiableSet(adjacentNodeValues.keySet());
        } else {
          return new AbstractSet<N>() {
            @Override
            public UnmodifiableIterator<N> iterator() {
              Iterator<NodeConnection<N>> nodeConnections = orderedNodeConnections.iterator();
              Set<N> seenNodes = new HashSet<>();
              return new AbstractIterator<N>() {
                @Override
                protected @Nullable N computeNext() {
                  while (nodeConnections.hasNext()) {
                    NodeConnection<N> nodeConnection = nodeConnections.next();
                    boolean added = seenNodes.add(nodeConnection.node);
                    if (added) {
                      return nodeConnection.node;
                    }
                  }
                  return endOfData();
                }
              };
            }
    
            @Override
            public int size() {
              return adjacentNodeValues.size();
            }
    
            @Override
            public boolean contains(@Nullable Object obj) {
              return adjacentNodeValues.containsKey(obj);
            }
          };
        }
      }
}
