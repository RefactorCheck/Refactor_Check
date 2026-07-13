public class zxing_0266 {

      private void adjustIncompleteIndicatorColumnRowNumbers(BarcodeMetadata barcodeMetadata) {
        BoundingBox boundingBox = getBoundingBox();
        ResultPoint top = isLeft ? boundingBox.getTopLeft() : boundingBox.getTopRight();
        ResultPoint bottom = isLeft ? boundingBox.getBottomLeft() : boundingBox.getBottomRight();
        int firstRow = imageRowToCodewordIndex((int) top.getY());
        int lastRow = imageRowToCodewordIndex((int) bottom.getY());
        processCodewords(firstRow, lastRow, barcodeMetadata);
      }

      private void processCodewords(int firstRow, int lastRow, BarcodeMetadata barcodeMetadata) {
        Codeword[] codewords = getCodewords();
        int barcodeRow = -1;
        int maxRowHeight = 1;
        int currentRowHeight = 0;
        for (int codewordsRow = firstRow; codewordsRow < lastRow; codewordsRow++) {
          if (codewords[codewordsRow] == null) {
            continue;
          }
          Codeword codeword = codewords[codewordsRow];

          codeword.setRowNumberAsRowIndicatorColumn();

          int rowDifference = codeword.getRowNumber() - barcodeRow;

          if (rowDifference == 0) {
            currentRowHeight++;
          } else if (rowDifference == 1) {
            maxRowHeight = Math.max(maxRowHeight, currentRowHeight);
            currentRowHeight = 1;
            barcodeRow = codeword.getRowNumber();
          } else if (codeword.getRowNumber() >= barcodeMetadata.getRowCount()) {
            codewords[codewordsRow] = null;
          } else {
            barcodeRow = codeword.getRowNumber();
            currentRowHeight = 1;
          }
        }
      }
}
