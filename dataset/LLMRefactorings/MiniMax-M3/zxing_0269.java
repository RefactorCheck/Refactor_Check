public class zxing_0269 {

      private void adjustRowNumbersFromBothRI() {
        if (detectionResultColumns[0] == null || detectionResultColumns[barcodeColumnCount + 1] == null) {
          return;
        }
        Codeword[] LRIcodewords = detectionResultColumns[0].getCodewords();
        Codeword[] RRIcodewords = detectionResultColumns[barcodeColumnCount + 1].getCodewords();
        for (int codewordsRow = 0; codewordsRow < LRIcodewords.length; codewordsRow++) {
          if (LRIcodewords[codewordsRow] != null &&
              RRIcodewords[codewordsRow] != null &&
              LRIcodewords[codewordsRow].getRowNumber() == RRIcodewords[codewordsRow].getRowNumber()) {
            for (int barcodeColumn = 1; barcodeColumn <= barcodeColumnCount; barcodeColumn++) {
              Codeword[] codewords = detectionResultColumns[barcodeColumn].getCodewords();
              Codeword codeword = codewords[codewordsRow];
              if (codeword == null) {
                continue;
              }
              codeword.setRowNumber(LRIcodewords[codewordsRow].getRowNumber());
              if (!codeword.hasValidRowNumber()) {
                codewords[codewordsRow] = null;
              }
            }
          }
        }
      }
}
