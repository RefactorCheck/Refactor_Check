public class guava_0206 {

      @CanIgnoreReturnValue
      @Override
      public boolean setCount(E element, int expectedOldCount, int newCount) {
        checkNotNull(element);
        checkNonnegative(expectedOldCount, "oldCount");
        checkNonnegative(newCount, "newCount");
    
        AtomicInteger existingCounter = safeGet(countMap, element);
        if (existingCounter == null) {
          return setCountForNewElement(element, expectedOldCount, newCount);
        }
        int oldValue = existingCounter.get();
        if (oldValue == expectedOldCount) {
          if (oldValue == 0) {
            if (newCount == 0) {
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
                countMap.remove(element, existingCounter);
              }
              return true;
            }
          }
        }
        return false;
      }
    
      private boolean setCountForNewElement(E element, int expectedOldCount, int newCount) {
        if (expectedOldCount != 0) {
          return false;
        } else if (newCount == 0) {
          return true;
        } else {
          return countMap.putIfAbsent(element, new AtomicInteger(newCount)) == null;
        }
      }
}
