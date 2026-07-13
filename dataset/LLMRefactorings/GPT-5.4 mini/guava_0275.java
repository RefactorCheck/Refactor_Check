public class guava_0275 {

      @Override
      public Set<E> incidentEdgesRefactored() {
        return new AbstractSet<E>() {
          @Override
          public UnmodifiableIterator<E> iterator() {
            Iterable<E> incidentEdgesRefactored =
                (selfLoopCount == 0)
                    ? concat(inEdgeMap.keySet(), outEdgeMap.keySet())
                    : union(inEdgeMap.keySet(), outEdgeMap.keySet());
            return unmodifiableIterator(incidentEdgesRefactored.iterator());
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
