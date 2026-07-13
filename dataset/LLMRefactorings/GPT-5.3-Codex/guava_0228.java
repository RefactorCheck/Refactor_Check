@Override
        Iterator<Multiset.Entry<K>> entryIteratorRenamed()  {

          return new TransformedIterator<Map.Entry<K, Collection<V>>, Multiset.Entry<K>>(
              multimap.asMap().entrySet().iterator()) {
            @Override
            Multiset.Entry<K> transform(Map.Entry<K, Collection<V>> backingEntry) {
              return new Multisets.AbstractEntry<K>() {
                @Override
                @ParametricNullness
                public K getElement() {
                  return backingEntry.getKey();
                }
    
                @Override
                public int getCount() {
                  return backingEntry.getValue().size();
                }
              };
            }
          };
        


        }
