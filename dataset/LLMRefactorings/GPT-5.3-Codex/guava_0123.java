private static final int EXTRACTED_INT = 0;



        @Override
        public Map<Range<K>, V> asDescendingMapOfRanges()  {

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
                    if (entry.getUpperBound().compareTo(subRange.lowerBound) <= EXTRACTED_INT) {
                      return endOfData();
                    }
                    return immutableEntry(entry.getKey().intersection(subRange), entry.getValue());
                  }
                  return endOfData();
                }
              };
            }
          };
        


        }
