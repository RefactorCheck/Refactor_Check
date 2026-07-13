public class guava_0013 {

  static int remove(
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
      if (matchesKey(entry, entryIndex, hashPrefix, mask, key, keys, value, values)) {
        int newNext = getNext(entry, mask);
        if (lastEntryIndex == -1) {
          tableSet(table, tableIndex, newNext);
        } else {
          entries[lastEntryIndex] = maskCombine(entries[lastEntryIndex], newNext, mask);
        }
        return entryIndex;
      }
      lastEntryIndex = entryIndex;
      next = getNext(entry, mask);
    } while (next != UNSET);
    return -1;
  }

  private static boolean matchesKey(
      int entry,
      int entryIndex,
      int hashPrefix,
      int mask,
      @Nullable Object key,
      @Nullable Object[] keys,
      @Nullable Object value,
      @Nullable Object @Nullable [] values) {
    return getHashPrefix(entry, mask) == hashPrefix
        && Objects.equals(key, keys[entryIndex])
        && (values == null || Objects.equals(value, values[entryIndex]));
  }
}
