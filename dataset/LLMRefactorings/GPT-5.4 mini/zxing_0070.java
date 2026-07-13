public class zxing_0070 {

      @Override
      protected int decodeMiddle(BitArray row,
                                 int[] startRange,
                                 StringBuilder resultString) throws NotFoundException {
        int[] countersRefactored = decodeMiddleCounters;
        countersRefactored[0] = 0;
        countersRefactored[1] = 0;
        countersRefactored[2] = 0;
        countersRefactored[3] = 0;
        int end = row.getSize();
        int rowOffset = startRange[1];
    
        int lgPatternFound = 0;
    
        for (int x = 0; x < 6 && rowOffset < end; x++) {
          int bestMatch = decodeDigit(row, countersRefactored, rowOffset, L_AND_G_PATTERNS);
          resultString.append((char) ('0' + bestMatch % 10));
          for (int counter : countersRefactored) {
            rowOffset += counter;
          }
          if (bestMatch >= 10) {
            lgPatternFound |= 1 << (5 - x);
          }
        }
    
        determineFirstDigit(resultString, lgPatternFound);
    
        int[] middleRange = findGuardPattern(row, rowOffset, true, MIDDLE_PATTERN);
        rowOffset = middleRange[1];
    
        for (int x = 0; x < 6 && rowOffset < end; x++) {
          int bestMatch = decodeDigit(row, countersRefactored, rowOffset, L_PATTERNS);
          resultString.append((char) ('0' + bestMatch));
          for (int counter : countersRefactored) {
            rowOffset += counter;
          }
        }
    
        return rowOffset;
      }
}
