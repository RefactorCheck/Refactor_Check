public class guava_0234 {

      @Override
      static Iterator<C> createColumnKeyIterator() {
        Comparator<? super C> comparator = columnComparator();
    
        Iterator<C> merged =
            mergeSorted(
                transform(backingMap.values(), (Map<C, V> input) -> input.keySet().iterator()),
                comparator);
    
        return new AbstractIterator<C>() {
          @Nullable C lastValue;
    
          @Override
          protected @Nullable C computeNext() {
            while (merged.hasNext()) {
              C next = merged.next();
              boolean duplicate = lastValue != null && comparator.compare(next, lastValue) == 0;
    
              // Keep looping till we find a non-duplicate value.
              if (!duplicate) {
                lastValue = next;
                return lastValue;
              }
            }
    
            lastValue = null; // clear reference to unused data
            return endOfData();
          }
        };
      }
}
