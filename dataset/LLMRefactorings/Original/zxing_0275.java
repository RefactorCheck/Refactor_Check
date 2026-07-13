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
              if (detectionResultColumns[barcodeColumn] == null) {
                formatter.format("    |   ");
                continue;
              }
              Codeword codeword = detectionResultColumns[barcodeColumn].getCodewords()[codewordsRow];
              if (codeword == null) {
                formatter.format("    |   ");
                continue;
              }
              formatter.format(" %3d|%3d", codeword.getRowNumber(), codeword.getValue());
            }
            formatter.format("%n");
          }
          return formatter.toString();
        }
      }
}
