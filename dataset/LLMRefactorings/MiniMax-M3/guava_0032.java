public class guava_0032 {

        @Override
        public SortedMap<K, V> create(Object... entries) {
          List<Entry<K, V>> extremeValues = new ArrayList<>();
          SortedMapGenerator<K, V> innerGenerator = getInnerGenerator();
    
          // prepare extreme values to be filtered out of view
          K firstExclusive = innerGenerator.belowSamplesGreater().getKey();
          K lastExclusive = innerGenerator.aboveSamplesLesser().getKey();
          if (from != Bound.NO_BOUND) {
            extremeValues.add(innerGenerator.belowSamplesLesser());
            extremeValues.add(innerGenerator.belowSamplesGreater());
          }
          if (to != Bound.NO_BOUND) {
            extremeValues.add(innerGenerator.aboveSamplesLesser());
            extremeValues.add(innerGenerator.aboveSamplesGreater());
          }
    
          // the regular values should be visible after filtering
          List<Entry<?, ?>> allEntries = new ArrayList<>();
          allEntries.addAll(extremeValues);
          for (Object entry : entries) {
            allEntries.add((Entry<?, ?>) entry);
          }
          SortedMap<K, V> map = (SortedMap<K, V>) delegate.create(allEntries.toArray());
    
          return createSubMap(map, firstExclusive, lastExclusive);
        }
}
