public class zxing_0129 {

      private int adjustRowNumbersFromRRI() {
        DetectionResultColumn rriColumn = detectionResultColumns[barcodeColumnCount + 1];
        if (rriColumn == null) {
          return 0;
        }
        int unadjustedCount = 0;
        Codeword[] codewords = rriColumn.getCodewords();
        for (int codewordsRow = 0; codewordsRow < codewords.length; codewordsRow++) {
          if (codewords[codewordsRow] == null) {
            continue;
          }
          int rowIndicatorRowNumber = codewords[codewordsRow].getRowNumber();
          int invalidRowCounts = 0;
          for (int barcodeColumn = barcodeColumnCount + 1;
               barcodeColumn > 0 && invalidRowCounts < ADJUST_ROW_NUMBER_SKIP;
               barcodeColumn--) {
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
