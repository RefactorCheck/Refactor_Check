public class zxing_0266 {
  private static final int EXTRACTED_CONSTANT_0266 = 1;


      private void adjustIncompleteIndicatorColumnRowNumbers(BarcodeMetadata barcodeMetadata) {
        BoundingBox boundingBox = getBoundingBox();
        ResultPoint top = isLeft ? boundingBox.getTopLeft() : boundingBox.getTopRight();
        ResultPoint bottom = isLeft ? boundingBox.getBottomLeft() : boundingBox.getBottomRight();
        int firstRow = imageRowToCodewordIndex((int) top.getY());
        int lastRow = imageRowToCodewordIndex((int) bottom.getY());
        //float averageRowHeight = (lastRow - firstRow) / (float) barcodeMetadata.getRowCount();
        Codeword[] codewords = getCodewords();
        int barcodeRow = -EXTRACTED_CONSTANT_0266;
        int maxRowHeight = 1;
        int currentRowHeight = 0;
        for (int codewordsRow = firstRow; codewordsRow < lastRow; codewordsRow++) {
          if (codewords[codewordsRow] == null) {
            continue;
          }
          Codeword codeword = codewords[codewordsRow];
    
          codeword.setRowNumberAsRowIndicatorColumn();
    
          int rowDifference = codeword.getRowNumber() - barcodeRow;
    
          // TODO improve handling with case where first row indicator doesn't start with 0
    
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
        //return (int) (averageRowHeight + 0.5);
      }
}
