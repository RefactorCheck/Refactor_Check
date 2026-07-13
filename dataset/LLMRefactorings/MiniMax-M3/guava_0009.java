public class guava_0009 {

      @Override
      public Set<N> successors() {
        return new AbstractSet<N>() {
          @Override
          public UnmodifiableIterator<N> iterator() {
            if (orderedNodeConnections == null) {
              return createUnorderedSuccessorIterator();
            } else {
              return createOrderedSuccessorIterator();
            }
          }
    
          @Override
          public int size() {
            return successorCount;
          }
    
          @Override
          public boolean contains(@Nullable Object obj) {
            return isSuccessor(adjacentNodeValues.get(obj));
          }
        };
      }

      private UnmodifiableIterator<N> createUnorderedSuccessorIterator() {
        Iterator<Entry<N, Object>> entries = adjacentNodeValues.entrySet().iterator();
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
      }

      private UnmodifiableIterator<N> createOrderedSuccessorIterator() {
        Iterator<NodeConnection<N>> nodeConnections = orderedNodeConnections.iterator();
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
