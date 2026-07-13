public class zxing_0108 {

      @Override
      protected int decodeMiddle(BitArray row,
                                 int[] startRange,
                                 StringBuilder result) throws NotFoundException {
        int[] counters = decodeMiddleCounters;
        counters[0] = 0;
        counters[1] = 0;
        counters[2] = 0;
        counters[3] = 0;
        int end = row.getSize();
        int rowOffset = startRange[1];

        rowOffset = decodeDigits(row, counters, rowOffset, end, result);

        int[] middleRange = findGuardPattern(row, rowOffset, true, MIDDLE_PATTERN);
        rowOffset = middleRange[1];

        rowOffset = decodeDigits(row, counters, rowOffset, end, result);

        return rowOffset;
      }

      private int decodeDigits(BitArray row, int[] counters, int rowOffset, int end, StringBuilder result) {
        for (int x = 0; x < 4 && rowOffset < end; x++) {
          int bestMatch = decodeDigit(row, counters, rowOffset, L_PATTERNS);
          result.append((char) ('0' + bestMatch));
          for (int counter : counters) {
            rowOffset += counter;
          }
        }
        return rowOffset;
      }
}
