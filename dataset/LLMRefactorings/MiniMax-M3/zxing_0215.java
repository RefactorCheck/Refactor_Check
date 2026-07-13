public class zxing_0215 {

      private int[] findFinderPattern(BitArray row, boolean rightFinderPattern)
          throws NotFoundException {
    
        int[] counters = getDecodeFinderCounters();
        resetCounters(counters);
    
        int width = row.getSize();
        boolean isWhite = false;
        int rowOffset = 0;
        while (rowOffset < width) {
          isWhite = !row.get(rowOffset);
          if (rightFinderPattern == isWhite) {
            // Will encounter white first when searching for right finder pattern
            break;
          }
          rowOffset++;
        }
    
        int counterPosition = 0;
        int patternStart = rowOffset;
        for (int x = rowOffset; x < width; x++) {
          if (row.get(x) != isWhite) {
            counters[counterPosition]++;
          } else {
            if (counterPosition == 3) {
              if (isFinderPattern(counters)) {
                return new int[]{patternStart, x};
              }
              patternStart += counters[0] + counters[1];
              counters[0] = counters[2];
              counters[1] = counters[3];
              counters[2] = 0;
              counters[3] = 0;
              counterPosition--;
            } else {
              counterPosition++;
            }
            counters[counterPosition] = 1;
            isWhite = !isWhite;
          }
        }
        throw NotFoundException.getNotFoundInstance();
    
      }

      private void resetCounters(int[] counters) {
        counters[0] = 0;
        counters[1] = 0;
        counters[2] = 0;
        counters[3] = 0;
      }
}
