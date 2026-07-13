public class zxing_0264 {

      void adjustCompleteIndicatorColumnRowNumbers(BarcodeMetadata barcodeMetadata) {
        Codeword[] codewords = getCodewords();
        setRowNumbers();
        removeIncorrectCodewords(codewords, barcodeMetadata);
        BoundingBox boundingBox = getBoundingBox();
        ResultPoint top = isLeft ? boundingBox.getTopLeft() : boundingBox.getTopRight();
        ResultPoint bottom = isLeft ? boundingBox.getBottomLeft() : boundingBox.getBottomRight();
        int firstRow = imageRowToCodewordIndex((int) top.getY());
        int lastRow = imageRowToCodewordIndex((int) bottom.getY());
        // We need to be careful using the average row height. Barcode could be skewed so that we have smaller and
        // taller rows
        //float averageRowHeight = (lastRow - firstRow) / (float) barcodeMetadata.getRowCount();
        int barcodeRow = -1;
        int maxRowHeight = 1;
        int currentRowHeight = 0;
        for (int codewordsRow = firstRow; codewordsRow < lastRow; codewordsRow++) {
          if (codewords[codewordsRow] == null) {
            continue;
          }
          Codeword codeword = codewords[codewordsRow];

          int rowDifference = codeword.getRowNumber() - barcodeRow;

          // TODO improve handling with case where first row indicator doesn't start with 0

          if (rowDifference == 0) {
            currentRowHeight++;
          } else if (rowDifference == 1) {
            maxRowHeight = Math.max(maxRowHeight, currentRowHeight);
            currentRowHeight = 1;
            barcodeRow = codeword.getRowNumber();
          } else if (rowDifference < 0 ||
                     codeword.getRowNumber() >= barcodeMetadata.getRowCount() ||
                     rowDifference > codewordsRow) {
            codewords[codewordsRow] = null;
          } else {
            int checkedRows;
            if (maxRowHeight > 2) {
              checkedRows = (maxRowHeight - 2) * rowDifference;
            } else {
              checkedRows = rowDifference;
            }
            boolean closePreviousCodewordFound = isClosePreviousCodewordFound(codewords, codewordsRow, checkedRows);
            if (closePreviousCodewordFound) {
              codewords[codewordsRow] = null;
            } else {
              barcodeRow = codeword.getRowNumber();
              currentRowHeight = 1;
            }
          }
        }
        //return (int) (averageRowHeight + 0.5);
      }

      private boolean isClosePreviousCodewordFound(Codeword[] codewords, int codewordsRow, int checkedRows) {
        if (checkedRows >= codewordsRow) {
          return true;
        }
        for (int i = 1; i <= checkedRows; i++) {
          // there must be (height * rowDifference) number of codewords missing. For now we assume height = 1.
          // This should hopefully get rid of most problems already.
          if (codewords[codewordsRow - i] != null) {
            return true;
          }
        }
        return false;
      }
}
