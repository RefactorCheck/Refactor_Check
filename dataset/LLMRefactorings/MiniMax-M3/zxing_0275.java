public class zxing_0275 {

      @Override
      public String toString() {
        DetectionResultColumn rowIndicatorColumn = detectionResultColumns[0];
        if (rowIndicatorColumn == null) {
          rowIndicatorColumn = detectionResultColumns[barcodeColumnCount + 1];
        }
        try (Formatter formatter = new Formatter()) {
          for (int codewordsRow = 0; codewordsRow < rowIndicatorColumn.getCodewords().length; codewordsRow++) {
            formatter.format("CW %3d:", codewordsRow);
            for (int barcodeColumn = 0; barcodeColumn < barcodeColumnCount + 2; barcodeColumn++) {
              appendCodeword(formatter, barcodeColumn, codewordsRow);
            }
            formatter.format("%n");
          }
          return formatter.toString();
        }
      }

      private void appendCodeword(Formatter formatter, int barcodeColumn, int codewordsRow) {
        if (detectionResultColumns[barcodeColumn] == null) {
          formatter.format("    |   ");
          return;
        }
        Codeword codeword = detectionResultColumns[barcodeColumn].getCodewords()[codewordsRow];
        if (codeword == null) {
          formatter.format("    |   ");
          return;
        }
        formatter.format(" %3d|%3d", codeword.getRowNumber(), codeword.getValue());
      }
}
