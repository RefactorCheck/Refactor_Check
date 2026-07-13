@Override
      public Set<N> successors()  {

        return new AbstractSet<N>() {
          @Override
          public UnmodifiableIterator<N> iterator() {
            if (orderedNodeConnections == null) {
              return new AbstractIterator<N>() {
                @Override
                protected @Nullable N computeNext() {
                  while (adjacentNodeValues.entrySet().iterator().hasNext()) {
                    Entry<N, Object> entry = entries.next();
                    if (isSuccessor(entry.getValue())) {
                      return entry.getKey();
                    }
                  }
                  return endOfData();
                }
              };
            } else {
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
