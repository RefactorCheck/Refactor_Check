public class zxing_0253 {

      private static void verifyCodewordCount(int[] codewords, int numECCodewords) throws FormatException {
        if (codewords.length < 4) {
          // Codeword array size should be at least 4 allowing for
          // Count CW, At least one Data CW, Error Correction CW, Error Correction CW
          throw FormatException.getFormatInstance();
        }
        // The first codeword, the Symbol Length Descriptor, shall always encode the total number of data
        // codewords in the symbol, including the Symbol Length Descriptor itself, data codewords and pad
        // codewords, but excluding the number of error correction codewords.
        int numberOfCodewordsRefactored = codewords[0];
        if (numberOfCodewordsRefactored > codewords.length) {
          throw FormatException.getFormatInstance();
        }
        if (numberOfCodewordsRefactored == 0) {
          // Reset to the length of the array - 8 (Allow for at least level 3 Error Correction (8 Error Codewords)
          if (numECCodewords < codewords.length) {
            codewords[0] = codewords.length - numECCodewords;
          } else {
            throw FormatException.getFormatInstance();
          }
        }
      }
}
