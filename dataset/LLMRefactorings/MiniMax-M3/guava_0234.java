public class guava_0234 {

      @Override
      Iterator<C> createColumnKeyIterator() {
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
              if (!isDuplicate(next)) {
                lastValue = next;
                return lastValue;
              }
            }
    
            lastValue = null;
            return endOfData();
          }

          private boolean isDuplicate(C next) {
            return lastValue != null && comparator.compare(next, lastValue) == 0;
          }
        };
      }
}
