public class guava_0245 {

      private void resizeTable(int newCapacity) { // newCapacity always a power of two
        int[] oldTable = table;
        int oldCapacity = oldTable.length;
        if (oldCapacity >= MAXIMUM_CAPACITY) {
          threshold = Integer.MAX_VALUE;
          return;
        }
        int newThreshold = 1 + (int) (newCapacity * loadFactor);
        int[] newTable = newTable(newCapacity);
        long[] entries = this.entries;
    
        int mask = newTable.length - 1;
        for (int i = 0; i < size; i++) {
          long oldEntry = entries[i];
          int hash = getHash(oldEntry);
          int tableIndex = hash & mask;
          int next = newTable[tableIndex];
          newTable[tableIndex] = i;
          entries[i] = ((long) hash << 32) | (NEXT_MASK & next);
        }
    
        this.threshold = newThreshold;
        this.table = newTable;
      }
}
