public class guava_0281 {

      private int remove(@Nullable Object key, int hash) {
        int tableIndex = hash & hashTableMask();
        int next = table[tableIndex];
        if (next == UNSET) { // empty bucket
          return 0;
        }
        int last = UNSET;
        do {
          if (getHash(entries[next]) == hash) {
            if (Objects.equals(key, keys[next])) {
              int oldValue = values[next];
              unlink(tableIndex, last, next);
              return oldValue;
            }
          }
          last = next;
          next = getNext(entries[next]);
        } while (next != UNSET);
        return 0;
      }

      private void unlink(int tableIndex, int last, int next) {
        if (last == UNSET) {
          table[tableIndex] = getNext(entries[next]);
        } else {
          entries[last] = swapNext(entries[last], getNext(entries[next]));
        }
        moveLastEntry(next);
        size--;
        modCount++;
      }
}
