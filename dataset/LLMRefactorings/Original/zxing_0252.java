public class zxing_0252 {

      private int adjustRowNumbersFromLRI() {
        if (detectionResultColumns[0] == null) {
          return 0;
        }
        int unadjustedCount = 0;
        Codeword[] codewords = detectionResultColumns[0].getCodewords();
        for (int codewordsRow = 0; codewordsRow < codewords.length; codewordsRow++) {
          if (codewords[codewordsRow] == null) {
            continue;
          }
          int rowIndicatorRowNumber = codewords[codewordsRow].getRowNumber();
          int invalidRowCounts = 0;
          for (int barcodeColumn = 1;
               barcodeColumn < barcodeColumnCount + 1 && invalidRowCounts < ADJUST_ROW_NUMBER_SKIP;
               barcodeColumn++) {
            Codeword codeword = detectionResultColumns[barcodeColumn].getCodewords()[codewordsRow];
            if (codeword != null) {
              invalidRowCounts = adjustRowNumberIfValid(rowIndicatorRowNumber, invalidRowCounts, codeword);
              if (!codeword.hasValidRowNumber()) {
                unadjustedCount++;
              }
            }
          }
        }
        return unadjustedCount;
      }
}
