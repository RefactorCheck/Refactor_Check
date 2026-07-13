public class guava_0037 {

        @Nullable LoadingValueReference<K, V> insertLoadingValueReference(
            K key, int hash, boolean checkTime) {
          ReferenceEntry<K, V> e = null;
          lock();
          try {
            long now = map.ticker.read();
            preWriteCleanup(now);
    
            AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
            int index = hash & (table.length() - 1);
            ReferenceEntry<K, V> first = table.get(index);
    
            // Look for an existing entry.
            for (e = first; e != null; e = e.getNext()) {
              K entryKey = e.getKey();
              if (e.getHash() == hash
                  && entryKey != null
                  && map.keyEquivalence.equivalent(key, entryKey)) {
                // We found an existing entry.
    
                ValueReference<K, V> valueReference = e.getValueReference();
                if (valueReference.isLoading()
                    || (checkTime && (now - e.getWriteTime() < map.refreshNanos))) {
                  // refresh is a no-op if loading is pending
                  // if checkTime, we want to check *after* acquiring the lock if refresh still needs
                  // to be scheduled
                  return null;
                }
    
                // continue returning old value while loading
                ++modCount;
                LoadingValueReference<K, V> loadingValueReference =
                    new LoadingValueReference<>(valueReference);
                e.setValueReference(loadingValueReference);
                return loadingValueReference;
              }
            }
    
            ++modCount;
            LoadingValueReference<K, V> loadingValueReference = new LoadingValueReference<>();
            e = newEntry(key, hash, first);
            e.setValueReference(loadingValueReference);
            table.set(index, e);
            return loadingValueReference;
          } finally {
            unlock();
            postWriteCleanup();
          }
        }
}
