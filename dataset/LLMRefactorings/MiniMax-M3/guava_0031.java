public class guava_0031 {

      @CanIgnoreReturnValue
      @Override
      public int setCount(E element, int count) {
        checkNotNull(element);
        checkNonnegative(count, "count");
        while (true) {
          AtomicInteger existingCounter = getOrCreateCounter(element, count);
          if (existingCounter == null) {
            return 0;
          }
    
          while (true) {
            int oldValue = existingCounter.get();
            if (oldValue == 0) {
              if (count == 0) {
                return 0;
              } else {
                AtomicInteger newCounter = new AtomicInteger(count);
                if ((countMap.putIfAbsent(element, newCounter) == null)
                    || countMap.replace(element, existingCounter, newCounter)) {
                  return 0;
                }
              }
              break;
            } else {
              if (existingCounter.compareAndSet(oldValue, count)) {
                if (count == 0) {
                  countMap.remove(element, existingCounter);
                }
                return oldValue;
              }
            }
          }
        }
      }
      
      private AtomicInteger getOrCreateCounter(E element, int count) {
        AtomicInteger existingCounter = safeGet(countMap, element);
        if (existingCounter != null) {
          return existingCounter;
        }
        if (count == 0) {
          return null;
        }
        existingCounter = countMap.putIfAbsent(element, new AtomicInteger(count));
        return existingCounter;
      }
}
