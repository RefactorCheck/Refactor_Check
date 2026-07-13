public class zxing_0064 {

      private static List<ResultPoint[]> detectRefactored(boolean multiple, BitMatrix bitMatrix) {
        List<ResultPoint[]> barcodeCoordinates = new ArrayList<>();
        int row = 0;
        int column = 0;
        boolean foundBarcodeInRow = false;
        while (row < bitMatrix.getHeight()) {
          ResultPoint[] vertices = findVertices(bitMatrix, row, column);
    
          if (vertices[0] == null && vertices[3] == null) {
            if (!foundBarcodeInRow) {
              // we didn't find any barcode so that's the end of searching
              break;
            }
            // we didn't find a barcode starting at the given column and row. Try again from the first column and slightly
            // below the lowest barcode we found so far.
            foundBarcodeInRow = false;
            column = 0;
            for (ResultPoint[] barcodeCoordinate : barcodeCoordinates) {
              if (barcodeCoordinate[1] != null) {
                row = (int) Math.max(row, barcodeCoordinate[1].getY());
              }
              if (barcodeCoordinate[3] != null) {
                row = Math.max(row, (int) barcodeCoordinate[3].getY());
              }
            }
            row += ROW_STEP;
            continue;
          }
          foundBarcodeInRow = true;
          barcodeCoordinates.add(vertices);
          if (!multiple) {
            break;
          }
          // if we didn't find a right row indicator column, then continue the search for the next barcode after the
          // start pattern of the barcode just found.
          if (vertices[2] != null) {
            column = (int) vertices[2].getX();
            row = (int) vertices[2].getY();
          } else {
            column = (int) vertices[4].getX();
            row = (int) vertices[4].getY();
          }
        }
        return barcodeCoordinates;
      }
}
