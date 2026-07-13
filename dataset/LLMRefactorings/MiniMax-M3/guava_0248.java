public class guava_0248 {

        @Override
        public SortedSet<E> create(Object... elements) {
          List<?> normalValues = asList(elements);
          List<E> extremeValues = new ArrayList<>();
    
          // nulls are usually out of bounds for a subset, so ban them altogether
          for (Object o : elements) {
            requireNonNull(o);
          }
    
          // prepare extreme values to be filtered out of view
          E firstExclusive = delegate.belowSamplesGreater();
          E lastExclusive = delegate.aboveSamplesLesser();
          addExtremeValues(extremeValues);
    
          // the regular values should be visible after filtering
          List<@Nullable Object> allEntries = new ArrayList<>();
          allEntries.addAll(extremeValues);
          allEntries.addAll(normalValues);
          SortedSet<E> set = delegate.create(allEntries.toArray());
    
          return createSubSet(set, firstExclusive, lastExclusive);
        }

        private void addExtremeValues(List<E> extremeValues) {
          if (from != Bound.NO_BOUND) {
            extremeValues.add(delegate.belowSamplesLesser());
            extremeValues.add(delegate.belowSamplesGreater());
          }
          if (to != Bound.NO_BOUND) {
            extremeValues.add(delegate.aboveSamplesLesser());
            extremeValues.add(delegate.aboveSamplesGreater());
          }
        }
}
