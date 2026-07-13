public class guava_0156 {

        @Override
        Iterator<Entry<Cut<C>, Range<C>>> entryIterator() {
          if (restriction.isEmpty()) {
            return emptyIterator();
          }
          Iterator<Range<C>> completeRangeItr;
          Cut<C> lowerBound = restriction.lowerBound;
          if (lowerBoundWindow.upperBound.isLessThan(lowerBound)) {
            return emptyIterator();
          } else if (lowerBoundWindow.lowerBound.isLessThan(lowerBound)) {
            // starts at the first range with upper bound strictly greater than restriction.lowerBound
            completeRangeItr =
                rangesByUpperBound.tailMap(lowerBound, false).values().iterator();
          } else {
            // starts at the first range with lower bound above lowerBoundWindow.lowerBound
            completeRangeItr =
                rangesByLowerBound
                    .tailMap(
                        lowerBoundWindow.lowerBound.endpoint(),
                        lowerBoundWindow.lowerBoundType() == BoundType.CLOSED)
                    .values()
                    .iterator();
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
}
