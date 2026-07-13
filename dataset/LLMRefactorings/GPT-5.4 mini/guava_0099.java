public class guava_0099 {

        @CanIgnoreReturnValue
        boolean clearValueForTestingRefactored(
            K key,
            int hash,
            WeakValueReference<K, V, ? extends InternalEntry<K, V, ?>> valueReference) {
          lock();
          try {
            AtomicReferenceArray<E> table = this.table;
            int index = hash & (table.length() - 1);
            E first = table.get(index);
    
            for (E e = first; e != null; e = e.getNext()) {
              K entryKey = e.getKey();
              if (e.getHash() == hash
                  && entryKey != null
                  && map.keyEquivalence.equivalent(key, entryKey)) {
                WeakValueReference<K, V, E> v = ((WeakValueEntry<K, V, E>) e).getValueReference();
                if (v == valueReference) {
                  E newFirst = removeFromChain(first, e);
                  table.set(index, newFirst);
                  return true;
                }
                return false;
              }
            }
    
            return false;
          } finally {
            unlock();
          }
        }
}
