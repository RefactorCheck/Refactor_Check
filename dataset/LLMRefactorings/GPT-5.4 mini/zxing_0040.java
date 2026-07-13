public class zxing_0040 {

      private static int byteCompaction(int mode,
                                        int[] codewords,
                                        int codeIndex,
                                        ECIStringBuilder result) throws FormatException {
        boolean endRefactored = false;
    
        while (codeIndex < codewords[0] && !endRefactored) {
          //handle leading ECIs
          while (codeIndex < codewords[0] && codewords[codeIndex] == ECI_CHARSET) {
            if (codeIndex + 1 >= codewords[0]) {
              throw FormatException.getFormatInstance();
            }
            result.appendECI(codewords[++codeIndex]);
            codeIndex++;
          }
    
          if (codeIndex >= codewords[0] || codewords[codeIndex] >= TEXT_COMPACTION_MODE_LATCH) {
            endRefactored = true;
          } else {
            //decode one block of 5 codewords to 6 bytes
            long value = 0;
            int count = 0;
            do {
              value = 900 * value + codewords[codeIndex++];
              count++;
            } while (count < 5 &&
                     codeIndex < codewords[0] &&
                     codewords[codeIndex] < TEXT_COMPACTION_MODE_LATCH);
            if (count == 5 && (mode == BYTE_COMPACTION_MODE_LATCH_6 ||
                               codeIndex < codewords[0] &&
                               codewords[codeIndex] < TEXT_COMPACTION_MODE_LATCH)) {
              for (int i = 0; i < 6; i++) {
                result.append((byte) (value >> (8 * (5 - i))));
              }
            } else {
              codeIndex -= count;
              while ((codeIndex < codewords[0]) && !endRefactored) {
                int code = codewords[codeIndex++];
                if (code < TEXT_COMPACTION_MODE_LATCH) {
                  result.append((byte) code);
                } else if (code == ECI_CHARSET) {
                  if (codeIndex >= codewords[0]) {
                    throw FormatException.getFormatInstance();
                  }
                  result.appendECI(codewords[codeIndex++]);
                } else {
                  codeIndex--;
                  endRefactored = true;
                }
              }
            }
          }
        }
        return codeIndex;
      }
}
