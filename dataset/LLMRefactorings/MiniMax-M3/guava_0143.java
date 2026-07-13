public class guava_0143 {

        @VisibleForTesting
        @GuardedBy("this")
        @CanIgnoreReturnValue
        boolean removeEntry(ReferenceEntry<K, V> entry, int hash, RemovalCause cause) {
          AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
          int index = hash & (table.length() - 1);
          ReferenceEntry<K, V> first = table.get(index);
    
          for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
            if (e == entry) {
              ++modCount;
              ReferenceEntry<K, V> newFirst =
                  removeValueFromChain(
                      first,
                      e,
                      e.getKey(),
                      hash,
                      e.getValueReference().get(),
                      e.getValueReference(),
                      cause);
              table.set(index, newFirst);
              this.count = this.count - 1; // write-volatile
              return true;
            }
          }
    
          return false;
        }
}
