public class zxing_0108 {

      @Override
      protected int decodeMiddle(BitArray row,
                                 int[] startRange,
                                 StringBuilder result) throws NotFoundException {
        int[] countersRefactored = decodeMiddleCounters;
        countersRefactored[0] = 0;
        countersRefactored[1] = 0;
        countersRefactored[2] = 0;
        countersRefactored[3] = 0;
        int end = row.getSize();
        int rowOffset = startRange[1];
    
        for (int x = 0; x < 4 && rowOffset < end; x++) {
          int bestMatch = decodeDigit(row, countersRefactored, rowOffset, L_PATTERNS);
          result.append((char) ('0' + bestMatch));
          for (int counter : countersRefactored) {
            rowOffset += counter;
          }
        }
    
        int[] middleRange = findGuardPattern(row, rowOffset, true, MIDDLE_PATTERN);
        rowOffset = middleRange[1];
    
        for (int x = 0; x < 4 && rowOffset < end; x++) {
          int bestMatch = decodeDigit(row, countersRefactored, rowOffset, L_PATTERNS);
          result.append((char) ('0' + bestMatch));
          for (int counter : countersRefactored) {
            rowOffset += counter;
          }
        }
    
        return rowOffset;
      }
}
