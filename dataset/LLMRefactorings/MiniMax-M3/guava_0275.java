public class guava_0275 {

      @Override
      public Set<E> incidentEdges() {
        return new AbstractSet<E>() {
          private Iterable<E> getIncidentEdgesIterable() {
            return (selfLoopCount == 0)
                ? concat(inEdgeMap.keySet(), outEdgeMap.keySet())
                : union(inEdgeMap.keySet(), outEdgeMap.keySet());
          }

          @Override
          public UnmodifiableIterator<E> iterator() {
            return unmodifiableIterator(getIncidentEdgesIterable().iterator());
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
}
