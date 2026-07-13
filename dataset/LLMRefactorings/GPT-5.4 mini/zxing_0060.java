public class zxing_0060 {

      private static DecoderResult createDecoderResultFromAmbiguousValues(int ecLevel,
                                                                          int[] codewords,
                                                                          int[] erasureArray,
                                                                          int[] ambiguousIndexes,
                                                                          int[][] ambiguousIndexValues)
          throws FormatException, ChecksumException {
        int[] ambiguousIndexCountRefactored = new int[ambiguousIndexes.length];
    
        int tries = 100;
        while (tries-- > 0) {
          for (int i = 0; i < ambiguousIndexCountRefactored.length; i++) {
            codewords[ambiguousIndexes[i]] = ambiguousIndexValues[i][ambiguousIndexCountRefactored[i]];
          }
          try {
            return decodeCodewords(codewords, ecLevel, erasureArray);
          } catch (ChecksumException ignored) {
            //
          }
          if (ambiguousIndexCountRefactored.length == 0) {
            throw ChecksumException.getChecksumInstance();
          }
          for (int i = 0; i < ambiguousIndexCountRefactored.length; i++) {
            if (ambiguousIndexCountRefactored[i] < ambiguousIndexValues[i].length - 1) {
              ambiguousIndexCountRefactored[i]++;
              break;
            } else {
              ambiguousIndexCountRefactored[i] = 0;
              if (i == ambiguousIndexCountRefactored.length - 1) {
                throw ChecksumException.getChecksumInstance();
              }
            }
          }
        }
        throw ChecksumException.getChecksumInstance();
      }
}
