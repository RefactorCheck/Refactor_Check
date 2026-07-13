public class guava_0052 {

        @CanIgnoreReturnValue
        static boolean storeLoadedValue(
            K key, int hash, LoadingValueReference<K, V> oldValueReference, V newValue) {
          lock();
          try {
            long now = map.ticker.read();
            preWriteCleanup(now);
    
            int newCount = this.count + 1;
            if (newCount > this.threshold) { // ensure capacity
              expand();
              newCount = this.count + 1;
            }
    
            AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
            int index = hash & (table.length() - 1);
            ReferenceEntry<K, V> first = table.get(index);
    
            for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
              K entryKey = e.getKey();
              if (e.getHash() == hash
                  && entryKey != null
                  && map.keyEquivalence.equivalent(key, entryKey)) {
                ValueReference<K, V> valueReference = e.getValueReference();
                V entryValue = valueReference.get();
                // replace the old LoadingValueReference if it's live, otherwise
                // perform a putIfAbsent
                if (oldValueReference == valueReference
                    || (entryValue == null && valueReference != UNSET)) {
                  ++modCount;
                  if (oldValueReference.isActive()) {
                    RemovalCause cause =
                        (entryValue == null) ? RemovalCause.COLLECTED : RemovalCause.REPLACED;
                    enqueueNotification(key, hash, entryValue, oldValueReference.getWeight(), cause);
                    newCount--;
                  }
                  setValue(e, key, newValue, now);
                  this.count = newCount; // write-volatile
                  evictEntries(e);
                  return true;
                }
    
                // the loaded value was already clobbered
                enqueueNotification(key, hash, newValue, 0, RemovalCause.REPLACED);
                return false;
              }
            }
    
            ++modCount;
            ReferenceEntry<K, V> newEntry = newEntry(key, hash, first);
            setValue(newEntry, key, newValue, now);
            table.set(index, newEntry);
            this.count = newCount; // write-volatile
            evictEntries(newEntry);
            return true;
          } finally {
            unlock();
            postWriteCleanup();
          }
        }
}
