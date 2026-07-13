public class guava_0037 {

        @Nullable LoadingValueReference<K, V> insertLoadingValueReference(
            K key, int hash, boolean checkTime) {
          lock();
          try {
            long now = map.ticker.read();
            preWriteCleanup(now);

            AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
            int index = hash & (table.length() - 1);
            ReferenceEntry<K, V> e = findExistingEntry(key, hash, table, index);

            if (e != null) {
              ValueReference<K, V> valueReference = e.getValueReference();
              if (valueReference.isLoading()
                  || (checkTime && (now - e.getWriteTime() < map.refreshNanos))) {
                return null;
              }

              ++modCount;
              LoadingValueReference<K, V> loadingValueReference =
                  new LoadingValueReference<>(valueReference);
              e.setValueReference(loadingValueReference);
              return loadingValueReference;
            }

            ++modCount;
            LoadingValueReference<K, V> loadingValueReference = new LoadingValueReference<>();
            e = newEntry(key, hash, table.get(index));
            e.setValueReference(loadingValueReference);
            table.set(index, e);
            return loadingValueReference;
          } finally {
            unlock();
            postWriteCleanup();
          }
        }

        private ReferenceEntry<K, V> findExistingEntry(
            K key, int hash, AtomicReferenceArray<ReferenceEntry<K, V>> table, int index) {
          for (ReferenceEntry<K, V> e = table.get(index); e != null; e = e.getNext()) {
            K entryKey = e.getKey();
            if (e.getHash() == hash
                && entryKey != null
                && map.keyEquivalence.equivalent(key, entryKey)) {
              return e;
            }
          }
          return null;
        }
}
