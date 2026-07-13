public class guava_0187 {

        @Override
        Iterator<Entry<Cut<C>, Range<C>>> entryIterator() {
          if (restriction.isEmpty()) {
            return emptyIterator();
          }
          Iterator<Range<C>> completeRangeItr = getStartingRangeIterator();
          if (completeRangeItr == null) {
            return emptyIterator();
          }
          Cut<Cut<C>> upperBoundOnLowerBounds =
              Ordering.<Cut<Cut<C>>>natural()
                  .min(lowerBoundWindow.upperBound, Cut.belowValue(restriction.upperBound));
          return new AbstractIterator<Entry<Cut<C>, Range<C>>>() {
            @Override
            protected @Nullable Entry<Cut<C>, Range<C>> computeNext() {
              if (!completeRangeItr.hasNext()) {
                return endOfData();
              }
              Range<C> nextRange = completeRangeItr.next();
              if (upperBoundOnLowerBounds.isLessThan(nextRange.lowerBound)) {
                return endOfData();
              } else {
                nextRange = nextRange.intersection(restriction);
                return immutableEntry(nextRange.lowerBound, nextRange);
              }
            }
          };
        }

        @Nullable
        private Iterator<Range<C>> getStartingRangeIterator() {
          if (lowerBoundWindow.upperBound.isLessThan(restriction.lowerBound)) {
            return null;
          } else if (lowerBoundWindow.lowerBound.isLessThan(restriction.lowerBound)) {
            return rangesByUpperBound.tailMap(restriction.lowerBound, false).values().iterator();
          } else {
            return rangesByLowerBound
                .tailMap(
                    lowerBoundWindow.lowerBound.endpoint(),
                    lowerBoundWindow.lowerBoundType() == BoundType.CLOSED)
                .values()
                .iterator();
          }
        }
}
