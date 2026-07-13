public class guava_0193 {

        @Override
        Collection<Collection<V>> createValues() {
          @WeakOuter
          final class ValuesImpl extends Maps.Values<K, Collection<V>> {
            ValuesImpl() {
              super(AsMap.this);
            }
    
            @Override
            /*
             * For discussion of equality in Multimap value collections, see the suppression for
             * UndefinedEquals in AbstractMapBasedMultimap.
             */
            @SuppressWarnings("UndefinedEquals")
            public boolean remove(@Nullable Object o) {
              if (o instanceof Collection) {
                return removeMatchingEntry((Collection<?>) o);
              }
              return false;
            }
    
            private boolean removeMatchingEntry(Collection<?> c) {
              Iterator<Entry<K, Collection<V>>> entryIterator =
                  unfiltered.asMap().entrySet().iterator();
              while (entryIterator.hasNext()) {
                Entry<K, Collection<V>> entry = entryIterator.next();
                K key = entry.getKey();
                Collection<V> collection =
                    filterCollection(entry.getValue(), new ValuePredicate(key));
                if (!collection.isEmpty() && c.equals(collection)) {
                  if (collection.size() == entry.getValue().size()) {
                    entryIterator.remove();
                  } else {
                    collection.clear();
                  }
                  return true;
                }
              }
              return false;
            }
    
            @Override
            public boolean removeAll(Collection<?> c) {
              return removeEntriesIf(valuePredicateOnEntries(in(c)));
            }
    
            @Override
            public boolean retainAll(Collection<?> c) {
              return removeEntriesIf(valuePredicateOnEntries(not(in(c))));
            }
          }
          return new ValuesImpl();
        }
}
