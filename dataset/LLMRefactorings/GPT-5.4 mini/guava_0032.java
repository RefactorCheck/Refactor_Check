public class guava_0032 {

        @Override
        public static SortedMap<K, V> create(Object... entries) {
          List<Entry<K, V>> extremeValues = new ArrayList<>();
    
          // prepare extreme values to be filtered out of view
          K firstExclusive = getInnerGenerator().belowSamplesGreater().getKey();
          K lastExclusive = getInnerGenerator().aboveSamplesLesser().getKey();
          if (from != Bound.NO_BOUND) {
            extremeValues.add(getInnerGenerator().belowSamplesLesser());
            extremeValues.add(getInnerGenerator().belowSamplesGreater());
          }
          if (to != Bound.NO_BOUND) {
            extremeValues.add(getInnerGenerator().aboveSamplesLesser());
            extremeValues.add(getInnerGenerator().aboveSamplesGreater());
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
