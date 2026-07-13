public class zxing_0124 {

    private static final int EXPECTED_WIDE_COUNTERS = 3;

    private static int toNarrowWidePattern(int[] counters) {
        int numCounters = counters.length;
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
            for (int i = 0; i < numCounters; i++) {
                int counter = counters[i];
                if (counter > maxNarrowCounter) {
                    pattern |= 1 << (numCounters - 1 - i);
                    wideCounters++;
                    totalWideCountersWidth += counter;
                }
            }
            if (wideCounters == EXPECTED_WIDE_COUNTERS) {
                for (int i = 0; i < numCounters && wideCounters > 0; i++) {
                    int counter = counters[i];
                    if (counter > maxNarrowCounter) {
                        wideCounters--;
                        if ((counter * 2) >= totalWideCountersWidth) {
                            return -1;
                        }
                    }
                }
                return pattern;
            }
        } while (wideCounters > EXPECTED_WIDE_COUNTERS);
        return -1;
    }
}
