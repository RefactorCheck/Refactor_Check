public class zxing_0204 {

      static int[] findStartGuardPattern(BitArray row) throws NotFoundException {
        boolean foundStart = false;
        int[] startRange = null;
        int nextStart = 0;
        int[] counters = new int[START_END_PATTERN.length];
        while (!foundStart) {
          Arrays.fill(counters, 0, START_END_PATTERN.length, 0);
          startRange = findGuardPattern(row, nextStart, false, START_END_PATTERN, counters);
          int start = startRange[0];
          nextStart = startRange[1];
          foundStart = hasValidQuietZone(row, start, nextStart);
        }
        return startRange;
      }

      private static boolean hasValidQuietZone(BitArray row, int start, int nextStart) {
        int quietStart = start - (nextStart - start);
        if (quietStart < 0) {
          return false;
        }
        return row.isRange(quietStart, start, false);
      }
}
