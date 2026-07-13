public class zxing_0070 {

      @Override
      protected int decodeMiddle(BitArray row,
                                 int[] startRange,
                                 StringBuilder resultString) throws NotFoundException {
        int[] counters = decodeMiddleCounters;
        counters[0] = 0;
        counters[1] = 0;
        counters[2] = 0;
        counters[3] = 0;
        int end = row.getSize();
        int rowOffset = startRange[1];
    
        int lgPatternFound = 0;
    
        for (int x = 0; x < 6 && rowOffset < end; x++) {
          int bestMatch = decodeDigit(row, counters, rowOffset, L_AND_G_PATTERNS);
          resultString.append((char) ('0' + bestMatch % 10));
          for (int counter : counters) {
            rowOffset += counter;
          }
          if (bestMatch >= 10) {
            lgPatternFound |= 1 << (5 - x);
          }
        }
    
        determineFirstDigit(resultString, lgPatternFound);
    
        int[] middleRange = findGuardPattern(row, rowOffset, true, MIDDLE_PATTERN);
        rowOffset = middleRange[1];
    
        rowOffset = decodeSixDigits(row, counters, rowOffset, end, resultString);
    
        return rowOffset;
      }
    
      private int decodeSixDigits(BitArray row, int[] counters, int rowOffset, int end, StringBuilder resultString) {
        for (int x = 0; x < 6 && rowOffset < end; x++) {
          int bestMatch = decodeDigit(row, counters, rowOffset, L_PATTERNS);
          resultString.append((char) ('0' + bestMatch));
          for (int counter : counters) {
            rowOffset += counter;
          }
        }
        return rowOffset;
      }
}
