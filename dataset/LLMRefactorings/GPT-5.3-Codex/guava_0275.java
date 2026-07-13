@Override
      public Set<E> incidentEdges()  {

        return new AbstractSet<E>() {
          @Override
          public UnmodifiableIterator<E> iterator() {
            Iterable<E> incidentEdges =
                (selfLoopCount == 0)
                    ? concat(inEdgeMap.keySet(), outEdgeMap.keySet())
                    : union(inEdgeMap.keySet(), outEdgeMap.keySet());
            Set<E> extractedValue = unmodifiableIterator(incidentEdges.iterator());
            return extractedValue;
          }
    
          @Override
          public int size() {
            return IntMath.saturatedAdd(inEdgeMap.size(), outEdgeMap.size() - selfLoopCount);
          }
    
          @Override
          public boolean contains(@Nullable Object obj) {
            return inEdgeMap.containsKey(obj) || outEdgeMap.containsKey(obj);
          }
        };
      


      }
