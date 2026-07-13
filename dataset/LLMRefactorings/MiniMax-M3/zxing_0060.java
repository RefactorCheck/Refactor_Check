public class zxing_0060 {

      private static final int MAX_TRIES = 100;

      private static DecoderResult createDecoderResultFromAmbiguousValues(int ecLevel,
                                                                          int[] codewords,
                                                                          int[] erasureArray,
                                                                          int[] ambiguousIndexes,
                                                                          int[][] ambiguousIndexValues)
          throws FormatException, ChecksumException {
        int[] ambiguousIndexCount = new int[ambiguousIndexes.length];

        int tries = MAX_TRIES;
        while (tries-- > 0) {
          for (int i = 0; i < ambiguousIndexCount.length; i++) {
            codewords[ambiguousIndexes[i]] = ambiguousIndexValues[i][ambiguousIndexCount[i]];
          }
          try {
            return decodeCodewords(codewords, ecLevel, erasureArray);
          } catch (ChecksumException ignored) {
            //
          }
          if (ambiguousIndexCount.length == 0) {
            throw ChecksumException.getChecksumInstance();
          }
          for (int i = 0; i < ambiguousIndexCount.length; i++) {
            if (ambiguousIndexCount[i] < ambiguousIndexValues[i].length - 1) {
              ambiguousIndexCount[i]++;
              break;
            } else {
              ambiguousIndexCount[i] = 0;
              if (i == ambiguousIndexCount.length - 1) {
                throw ChecksumException.getChecksumInstance();
              }
            }
          }
        }
        throw ChecksumException.getChecksumInstance();
      }
}
