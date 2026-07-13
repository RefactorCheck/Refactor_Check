private Iterator<Entry<K, V>> entryIterator = backingMap.entrySet().iterator();



      final Set<K> unmodifiableKeySet()  {

        return new AbstractSet<K>() {
          @Override
          public UnmodifiableIterator<K> iterator() {
    
            return new UnmodifiableIterator<K>() {
              @Override
              public boolean hasNext() {
                return entryIterator.hasNext();
              }
    
              @Override
              public K next() {
                Entry<K, V> entry = entryIterator.next(); // store local reference for thread-safety
                cacheEntry = entry;
                return entry.getKey();
              }
            };
          }
    
          @Override
          public int size() {
            return backingMap.size();
          }
    
          @Override
          public boolean contains(@Nullable Object key) {
            return containsKey(key);
          }
        };
      


      }
