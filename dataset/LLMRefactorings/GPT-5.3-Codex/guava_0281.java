private static int remove(@Nullable Object key, int hash)  {

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
    
              if (last == UNSET) {
                // we need to update the root link from table[]
                table[tableIndex] = getNext(entries[next]);
              } else {
                // we need to update the link from the chain
                entries[last] = swapNext(entries[last], getNext(entries[next]));
              }
    
              moveLastEntry(next);
              size--;
              modCount++;
              return oldValue;
            }
          }
          last = next;
          next = getNext(entries[next]);
        } while (next != UNSET);
        return 0;
      


      }
