public class guava_0238 {

        @CanIgnoreReturnValue
        boolean reclaimValue(K key, int hash, ValueReference<K, V> valueReference) {
          lock();
          try {
            int newCount = this.count - 1;
            AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
            int index = hash & (table.length() - 1);
            ReferenceEntry<K, V> first = table.get(index);
    
            for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
              K entryKey = e.getKey();
              if (e.getHash() == hash
                  && entryKey != null
                  && map.keyEquivalence.equivalent(key, entryKey)) {
                ValueReference<K, V> v = e.getValueReference();
                if (v == valueReference) {
                  return reclaimMatchedEntry(
                      first, e, entryKey, hash, valueReference, index, newCount);
                }
                return false;
              }
            }
    
            return false;
          } finally {
            unlock();
            if (!isHeldByCurrentThread()) { // don't clean up inside of put
              postWriteCleanup();
            }
          }
        }
    
        private boolean reclaimMatchedEntry(
            ReferenceEntry<K, V> first,
            ReferenceEntry<K, V> e,
            K entryKey,
            int hash,
            ValueReference<K, V> valueReference,
            int index,
            int newCount) {
          ++modCount;
          AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
          ReferenceEntry<K, V> newFirst =
              removeValueFromChain(
                  first,
                  e,
                  entryKey,
                  hash,
                  valueReference.get(),
                  valueReference,
                  RemovalCause.COLLECTED);
          newCount = this.count - 1;
          table.set(index, newFirst);
          this.count = newCount; // write-volatile
          return true;
        }
}
