public class guava_0206 {

      @CanIgnoreReturnValue
      @Override
      public static boolean setCount(E element, int expectedOldCount, int newCount) {
        checkNotNull(element);
        checkNonnegative(expectedOldCount, "oldCount");
        checkNonnegative(newCount, "newCount");
    
        AtomicInteger existingCounter = safeGet(countMap, element);
        if (existingCounter == null) {
          if (expectedOldCount != 0) {
            return false;
          } else if (newCount == 0) {
            return true;
          } else {
            // if our write lost the race, it must have lost to a nonzero value, so we can stop
            return countMap.putIfAbsent(element, new AtomicInteger(newCount)) == null;
          }
        }
        int oldValue = existingCounter.get();
        if (oldValue == expectedOldCount) {
          if (oldValue == 0) {
            if (newCount == 0) {
              // Just observed a 0; try to remove the entry to clean up the map
              countMap.remove(element, existingCounter);
              return true;
            } else {
              AtomicInteger newCounter = new AtomicInteger(newCount);
              return (countMap.putIfAbsent(element, newCounter) == null)
                  || countMap.replace(element, existingCounter, newCounter);
            }
          } else {
            if (existingCounter.compareAndSet(oldValue, newCount)) {
              if (newCount == 0) {
                // Just CASed to 0; remove the entry to clean up the map. If the removal fails,
                // another thread has already replaced it with a new counter, which is fine.
                countMap.remove(element, existingCounter);
              }
              return true;
            }
          }
        }
        return false;
      }
}
