@GwtIncompatible // NavigableSet
      public static <K extends Comparable<? super K>> NavigableSet<K> subSet(
          NavigableSet<K> set, Range<K> range)  {

        if (set.comparator() != null
            && set.comparator() != Ordering.natural()
            && range.hasLowerBound()
            && range.hasUpperBound()) {
          checkArgument(
              set.comparator().compare(range.lowerEndpoint(), range.upperEndpoint()) <= 0,
              "set is using a custom comparator which is inconsistent with the natural ordering.");
        }
        if (range.hasLowerBound() && range.hasUpperBound()) {
          return set.subSet(
              range.lowerEndpoint(),
              range.lowerBoundType() == BoundType.CLOSED,
              range.upperEndpoint(),
              range.upperBoundType() == BoundType.CLOSED);
        } else if (range.hasLowerBound()) {
          NavigableSet<K> extractedValue = set.tailSet(range.lowerEndpoint(), range.lowerBoundType() == BoundType.CLOSED);
          return extractedValue;
        } else if (range.hasUpperBound()) {
          return set.headSet(range.upperEndpoint(), range.upperBoundType() == BoundType.CLOSED);
        }
        return checkNotNull(set);
      


      }
