public class guava_0123 {

        @Override
        public Map<Range<K>, V> asDescendingMapOfRanges() {
          return new SubRangeMapAsMap() {
    
            @Override
            Iterator<Entry<Range<K>, V>> entryIterator() {
              if (subRange.isEmpty()) {
                return emptyIterator();
              }
              Iterator<RangeMapEntry<K, V>> backingItr =
                  entriesByLowerBound
                      .headMap(subRange.upperBound, false)
                      .descendingMap()
                      .values()
                      .iterator();
              return new AbstractIterator<Entry<Range<K>, V>>() {
    
                @Override
                protected @Nullable Entry<Range<K>, V> computeNext() {
                  if (backingItr.hasNext()) {
                    RangeMapEntry<K, V> entry = backingItr.next();
                    if (entry.getUpperBound().compareTo(subRange.lowerBound) <= 0) {
                      return endOfData();
                    }
                    Range<K> intersectedKey = entry.getKey().intersection(subRange);
                    return immutableEntry(intersectedKey, entry.getValue());
                  }
                  return endOfData();
                }
              };
            }
          };
        }
}
