public class guava_0227 {

  @Override
  public SortedMultiset<E> create(Object... entries) {
    @SuppressWarnings("unchecked")
    List<E> extremeValues = (List<E>) getExtremeValues();
    @SuppressWarnings("unchecked")
    List<E> normalValues = (List<E>) asList(entries);

    sort(extremeValues, comparator);
    E firstExclusive = extremeValues.get(1);
    E lastExclusive = extremeValues.get(2);
    if (from == Bound.NO_BOUND) {
      extremeValues.remove(0);
      extremeValues.remove(0);
    }
    if (to == Bound.NO_BOUND) {
      extremeValues.remove(extremeValues.size() - 1);
      extremeValues.remove(extremeValues.size() - 1);
    }

    List<E> allEntries = new ArrayList<>();
    allEntries.addAll(extremeValues);
    allEntries.addAll(normalValues);
    SortedMultiset<E> multiset =
        (SortedMultiset<E>) delegate.create(allEntries.toArray());

    return applyBounds(multiset, firstExclusive, lastExclusive);
  }

  private SortedMultiset<E> applyBounds(SortedMultiset<E> multiset, E firstExclusive, E lastExclusive) {
    if (from == Bound.INCLUSIVE) {
      multiset = multiset.tailMultiset(firstInclusive, BoundType.CLOSED);
    } else if (from == Bound.EXCLUSIVE) {
      multiset = multiset.tailMultiset(firstExclusive, BoundType.OPEN);
    }

    if (to == Bound.INCLUSIVE) {
      multiset = multiset.headMultiset(lastInclusive, BoundType.CLOSED);
    } else if (to == Bound.EXCLUSIVE) {
      multiset = multiset.headMultiset(lastExclusive, BoundType.OPEN);
    }

    return multiset;
  }
}
