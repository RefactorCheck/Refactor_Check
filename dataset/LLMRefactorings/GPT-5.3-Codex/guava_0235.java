@Override
            /*
             * For discussion of equality in Multimap value collections, see the suppression for
             * UndefinedEquals in AbstractMapBasedMultimap.
             */
            @SuppressWarnings("UndefinedEquals")
            public boolean remove(@Nullable Object o)  {

              if (o instanceof Collection) {
                Collection<?> c = (Collection<?>) o;
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
              }
              return false;
            


            }
