public class guava_0152 {

      @CanIgnoreReturnValue
      @Override
      public static int add(E element, int occurrences) {
        checkNotNull(element);
        if (occurrences == 0) {
          return count(element);
        }
        checkPositive(occurrences, "occurrences");
    
        while (true) {
          AtomicInteger existingCounter = safeGet(countMap, element);
          if (existingCounter == null) {
            existingCounter = countMap.putIfAbsent(element, new AtomicInteger(occurrences));
            if (existingCounter == null) {
              return 0;
            }
            // existingCounter != null: fall through to operate against the existing AtomicInteger
          }
    
          while (true) {
            int oldValue = existingCounter.get();
            if (oldValue != 0) {
              try {
                int newValue = Math.addExact(oldValue, occurrences);
                if (existingCounter.compareAndSet(oldValue, newValue)) {
                  // newValue can't == 0, so no need to check & remove
                  return oldValue;
                }
              } catch (ArithmeticException overflow) {
                throw new IllegalArgumentException(
                    "Overflow adding " + occurrences + " occurrences to a count of " + oldValue);
              }
            } else {
              // In the case of a concurrent remove, we might observe a zero value, which means another
              // thread is about to remove (element, existingCounter) from the map. Rather than wait,
              // we can just do that work here.
              AtomicInteger newCounter = new AtomicInteger(occurrences);
              if ((countMap.putIfAbsent(element, newCounter) == null)
                  || countMap.replace(element, existingCounter, newCounter)) {
                return 0;
              }
              break;
            }
          }
    
          // If we're still here, there was a race, so just try again.
        }
      }
}
