public class guava_0074 {

        @Override
        public Range<K> span() {
          Cut<K> lowerBound = computeLowerBound();
          Cut<K> upperBound = computeUpperBound();
          return Range.create(lowerBound, upperBound);
        }

        private Cut<K> computeLowerBound() {
          Entry<Cut<K>, RangeMapEntry<K, V>> lowerEntry =
              entriesByLowerBound.floorEntry(subRange.lowerBound);
          if (lowerEntry != null
              && lowerEntry.getValue().getUpperBound().compareTo(subRange.lowerBound) > 0) {
            return subRange.lowerBound;
          }
          Cut<K> lowerBound = entriesByLowerBound.ceilingKey(subRange.lowerBound);
          if (lowerBound == null || lowerBound.compareTo(subRange.upperBound) >= 0) {
            throw new NoSuchElementException();
          }
          return lowerBound;
        }

        private Cut<K> computeUpperBound() {
          Entry<Cut<K>, RangeMapEntry<K, V>> upperEntry =
              entriesByLowerBound.lowerEntry(subRange.upperBound);
          if (upperEntry == null) {
            throw new NoSuchElementException();
          } else if (upperEntry.getValue().getUpperBound().compareTo(subRange.upperBound) >= 0) {
            return subRange.upperBound;
          }
          return upperEntry.getValue().getUpperBound();
        }
}
