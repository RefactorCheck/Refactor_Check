public class zxing_0237 {

      private int toNarrowWidePattern(int position) {
        int end = position + 7;
        if (end >= counterLength) {
          return -1;
        }
    
        int[] theCounters = counters;
    
        int[] barMinMax = findMinMax(theCounters, position, end, 2);
        int thresholdBar = (barMinMax[0] + barMinMax[1]) / 2;
    
        int[] spaceMinMax = findMinMax(theCounters, position + 1, end, 2);
        int thresholdSpace = (spaceMinMax[0] + spaceMinMax[1]) / 2;
    
        int bitmask = 1 << 7;
        int pattern = 0;
        for (int i = 0; i < 7; i++) {
          int threshold = (i & 1) == 0 ? thresholdBar : thresholdSpace;
          bitmask >>= 1;
          if (theCounters[position + i] > threshold) {
            pattern |= bitmask;
          }
        }
    
        for (int i = 0; i < CHARACTER_ENCODINGS.length; i++) {
          if (CHARACTER_ENCODINGS[i] == pattern) {
            return i;
          }
        }
        return -1;
      }
    
      private int[] findMinMax(int[] theCounters, int start, int end, int step) {
        int max = 0;
        int min = Integer.MAX_VALUE;
        for (int j = start; j < end; j += step) {
          int currentCounter = theCounters[j];
          if (currentCounter < min) {
            min = currentCounter;
          }
          if (currentCounter > max) {
            max = currentCounter;
          }
        }
        return new int[]{min, max};
      }
}
