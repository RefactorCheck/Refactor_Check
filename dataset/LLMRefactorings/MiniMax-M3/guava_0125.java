public class guava_0125 {

        @Nullable V replace(K key, int hash, V newValue) {
          lock();
          try {
            long now = map.ticker.read();
            preWriteCleanup(now);
    
            AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
            int index = hash & (table.length() - 1);
            ReferenceEntry<K, V> first = table.get(index);
    
            for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
              K entryKey = e.getKey();
              if (keyMatches(e, hash, key, entryKey)) {
                ValueReference<K, V> valueReference = e.getValueReference();
                V entryValue = valueReference.get();
                if (entryValue == null) {
                  if (valueReference.isActive()) {
                    // If the value disappeared, this entry is partially collected.
                    int newCount = this.count - 1;
                    ++modCount;
                    ReferenceEntry<K, V> newFirst =
                        removeValueFromChain(
                            first,
                            e,
                            entryKey,
                            hash,
                            entryValue,
                            valueReference,
                            RemovalCause.COLLECTED);
                    newCount = this.count - 1;
                    table.set(index, newFirst);
                    this.count = newCount; // write-volatile
                  }
                  return null;
                }
    
                ++modCount;
                enqueueNotification(
                    key, hash, entryValue, valueReference.getWeight(), RemovalCause.REPLACED);
                setValue(e, key, newValue, now);
                evictEntries(e);
                return entryValue;
              }
            }
    
            return null;
          } finally {
            unlock();
            postWriteCleanup();
          }
        }
        
        private boolean keyMatches(ReferenceEntry<K, V> e, int hash, K key, K entryKey) {
          return e.getHash() == hash
              && entryKey != null
              && map.keyEquivalence.equivalent(key, entryKey);
        }
}
