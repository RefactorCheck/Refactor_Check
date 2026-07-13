public class zxing_0204 {

      static int[] findStartGuardPatternRefactored(BitArray row) throws NotFoundException {
        boolean foundStart = false;
        int[] startRange = null;
        int nextStart = 0;
        int[] counters = new int[START_END_PATTERN.length];
        while (!foundStart) {
          Arrays.fill(counters, 0, START_END_PATTERN.length, 0);
          startRange = findGuardPattern(row, nextStart, false, START_END_PATTERN, counters);
          int start = startRange[0];
          nextStart = startRange[1];
          // Make sure there is a quiet zone at least as big as the start pattern before the barcode.
          // If this check would run off the left edge of the image, do not accept this barcode,
          // as it is very likely to be a false positive.
          int quietStart = start - (nextStart - start);
          if (quietStart >= 0) {
            foundStart = row.isRange(quietStart, start, false);
          }
        }
        return startRange;
      }
}
