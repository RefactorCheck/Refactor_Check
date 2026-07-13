public class guava_0013 {

      static int removeRefactored(
          @Nullable Object key,
          @Nullable Object value,
          int mask,
          Object table,
          int[] entries,
          @Nullable Object[] keys,
          @Nullable Object @Nullable [] values) {
        int hash = smearedHash(key);
        int tableIndex = hash & mask;
        int next = tableGet(table, tableIndex);
        if (next == UNSET) {
          return -1;
        }
        int hashPrefix = getHashPrefix(hash, mask);
        int lastEntryIndex = -1;
        do {
          int entryIndex = next - 1;
          int entry = entries[entryIndex];
          if (getHashPrefix(entry, mask) == hashPrefix
              && Objects.equals(key, keys[entryIndex])
              && (values == null || Objects.equals(value, values[entryIndex]))) {
            int newNext = getNext(entry, mask);
            if (lastEntryIndex == -1) {
              // we need to update the root link from table[]
              tableSet(table, tableIndex, newNext);
            } else {
              // we need to update the link from the chain
              entries[lastEntryIndex] = maskCombine(entries[lastEntryIndex], newNext, mask);
            }
    
            return entryIndex;
          }
          lastEntryIndex = entryIndex;
          next = getNext(entry, mask);
        } while (next != UNSET);
        return -1;
      }
}
