public class zxing_0263 {

      private int decodeMiddle(BitArray row, int[] startRange, StringBuilder resultString) throws NotFoundException {
        int[] counters = decodeMiddleCounters;
        counters[0] = 0;
        counters[1] = 0;
        counters[2] = 0;
        counters[3] = 0;
        int end = row.getSize();
        int rowOffset = startRange[1];
    
        int lgPatternFound = 0;
    
        for (int x = 0; x < 5 && rowOffset < end; x++) {
          int bestMatch = UPCEANReader.decodeDigit(row, counters, rowOffset, UPCEANReader.L_AND_G_PATTERNS);
          resultString.append((char) ('0' + bestMatch % 10));
          for (int counter : counters) {
            rowOffset += counter;
          }
          if (bestMatch >= 10) {
            lgPatternFound |= 1 << (4 - x);
          }
          if (x != 4) {
            rowOffset = skipSeparator(row, rowOffset);
          }
        }
    
        if (resultString.length() != 5) {
          throw NotFoundException.getNotFoundInstance();
        }
    
        int checkDigit = determineCheckDigit(lgPatternFound);
        if (extensionChecksum(resultString.toString()) != checkDigit) {
          throw NotFoundException.getNotFoundInstance();
        }
    
        return rowOffset;
      }

      private int skipSeparator(BitArray row, int rowOffset) {
        rowOffset = row.getNextSet(rowOffset);
        return row.getNextUnset(rowOffset);
      }
}
