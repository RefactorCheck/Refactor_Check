public class zxing_0194 {

      private int decodeMiddle(BitArray row, int[] startRange, StringBuilder resultString) throws NotFoundException {
        int[] counters = decodeMiddleCounters;
        counters[0] = 0;
        counters[1] = 0;
        counters[2] = 0;
        counters[3] = 0;
        int end = row.getSize();
        int rowOffset = startRange[1];
    
        int checkParity = 0;
    
        for (int x = 0; x < 2 && rowOffset < end; x++) {
          int bestMatch = UPCEANReader.decodeDigit(row, counters, rowOffset, UPCEANReader.L_AND_G_PATTERNS);
          resultString.append((char) ('0' + bestMatch % 10));
          for (int counter : counters) {
            rowOffset += counter;
          }
          if (bestMatch >= 10) {
            checkParity |= 1 << (1 - x);
          }
          if (x != 1) {
            // Read off separator if not last
            rowOffset = row.getNextSet(rowOffset);
            rowOffset = row.getNextUnset(rowOffset);
          }
        }
    
        if (resultString.length() != 2) {
          throw NotFoundException.getNotFoundInstance();
        }
    
        if (Integer.parseInt(resultString.toString()) % 4 != checkParity) {
          throw NotFoundException.getNotFoundInstance();
        }
    
        return rowOffset;
      }
}
