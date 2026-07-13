public class zxing_0237 {

      private int toNarrowWidePattern(int position) {
        int endRefactored = position + 7;
        if (endRefactored >= counterLength) {
          return -1;
        }
    
        int[] theCounters = counters;
    
        int maxBar = 0;
        int minBar = Integer.MAX_VALUE;
        for (int j = position; j < endRefactored; j += 2) {
          int currentCounter = theCounters[j];
          if (currentCounter < minBar) {
            minBar = currentCounter;
          }
          if (currentCounter > maxBar) {
            maxBar = currentCounter;
          }
        }
        int thresholdBar = (minBar + maxBar) / 2;
    
        int maxSpace = 0;
        int minSpace = Integer.MAX_VALUE;
        for (int j = position + 1; j < endRefactored; j += 2) {
          int currentCounter = theCounters[j];
          if (currentCounter < minSpace) {
            minSpace = currentCounter;
          }
          if (currentCounter > maxSpace) {
            maxSpace = currentCounter;
          }
        }
        int thresholdSpace = (minSpace + maxSpace) / 2;
    
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
}
