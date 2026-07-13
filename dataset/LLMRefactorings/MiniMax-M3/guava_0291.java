public class guava_0291 {

        @VisibleForTesting
        @GuardedBy("this")
        @CanIgnoreReturnValue
        boolean removeEntry(ReferenceEntry<K, V> entry, int hash, RemovalCause cause) {
          AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
          int index = hash & (table.length() - 1);
          ReferenceEntry<K, V> first = table.get(index);
    
          for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
            if (e == entry) {
              removeMatchedEntry(first, e, hash, cause, table, index);
              return true;
            }
          }
    
          return false;
        }

        private void removeMatchedEntry(
            ReferenceEntry<K, V> first,
            ReferenceEntry<K, V> entry,
            int hash,
            RemovalCause cause,
            AtomicReferenceArray<ReferenceEntry<K, V>> table,
            int index) {
          ++modCount;
          ReferenceEntry<K, V> newFirst =
              removeValueFromChain(
                  first,
                  entry,
                  entry.getKey(),
                  hash,
                  entry.getValueReference().get(),
                  entry.getValueReference(),
                  cause);
          int newCount = this.count - 1;
          table.set(index, newFirst);
          this.count = newCount; // write-volatile
        }
}
