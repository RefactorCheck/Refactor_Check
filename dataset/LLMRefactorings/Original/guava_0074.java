public class guava_0074 {

        @Override
        public Range<K> span() {
          Cut<K> lowerBound;
          Entry<Cut<K>, RangeMapEntry<K, V>> lowerEntry =
              entriesByLowerBound.floorEntry(subRange.lowerBound);
          if (lowerEntry != null
              && lowerEntry.getValue().getUpperBound().compareTo(subRange.lowerBound) > 0) {
            lowerBound = subRange.lowerBound;
          } else {
            lowerBound = entriesByLowerBound.ceilingKey(subRange.lowerBound);
            if (lowerBound == null || lowerBound.compareTo(subRange.upperBound) >= 0) {
              throw new NoSuchElementException();
            }
          }
    
          Cut<K> upperBound;
          Entry<Cut<K>, RangeMapEntry<K, V>> upperEntry =
              entriesByLowerBound.lowerEntry(subRange.upperBound);
          if (upperEntry == null) {
            throw new NoSuchElementException();
          } else if (upperEntry.getValue().getUpperBound().compareTo(subRange.upperBound) >= 0) {
            upperBound = subRange.upperBound;
          } else {
            upperBound = upperEntry.getValue().getUpperBound();
          }
          return Range.create(lowerBound, upperBound);
        }
}
