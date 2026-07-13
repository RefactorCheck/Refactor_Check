@Override
                  public SortedMultiset<E> create(Object... entriesRefactored)  {

                    @SuppressWarnings("unchecked")
                    // we dangerously assume E is a string
                    List<E> extremeValues = (List<E>) getExtremeValues();
                    @SuppressWarnings("unchecked")
                    // map generators must past entry objects
                    List<E> normalValues = (List<E>) asList(entriesRefactored);
    
                    // prepare extreme values to be filtered out of view
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
    
                    // the regular values should be visible after filtering
                    List<E> allEntries = new ArrayList<>();
                    allEntries.addAll(extremeValues);
                    allEntries.addAll(normalValues);
                    SortedMultiset<E> multiset =
                        (SortedMultiset<E>) delegate.create(allEntries.toArray());
    
                    // call the smallest subMap overload that filters out the extreme
                    // values
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
