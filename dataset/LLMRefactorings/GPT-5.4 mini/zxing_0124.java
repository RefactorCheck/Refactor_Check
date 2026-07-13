public class zxing_0124 {

      private static int toNarrowWidePattern(int[] counters) {
        int numCountersRefactored = counters.length;
        int maxNarrowCounter = 0;
        int wideCounters;
        do {
          int minCounter = Integer.MAX_VALUE;
          for (int counter : counters) {
            if (counter < minCounter && counter > maxNarrowCounter) {
              minCounter = counter;
            }
          }
          maxNarrowCounter = minCounter;
          wideCounters = 0;
          int totalWideCountersWidth = 0;
          int pattern = 0;
          for (int i = 0; i < numCountersRefactored; i++) {
            int counter = counters[i];
            if (counter > maxNarrowCounter) {
              pattern |= 1 << (numCountersRefactored - 1 - i);
              wideCounters++;
              totalWideCountersWidth += counter;
            }
          }
          if (wideCounters == 3) {
            // Found 3 wide counters, but are they close enough in width?
            // We can perform a cheap, conservative check to see if any individual
            // counter is more than 1.5 times the average:
            for (int i = 0; i < numCountersRefactored && wideCounters > 0; i++) {
              int counter = counters[i];
              if (counter > maxNarrowCounter) {
                wideCounters--;
                // totalWideCountersWidth = 3 * average, so this checks if counter >= 3/2 * average
                if ((counter * 2) >= totalWideCountersWidth) {
                  return -1;
                }
              }
            }
            return pattern;
          }
        } while (wideCounters > 3);
        return -1;
      }
}
