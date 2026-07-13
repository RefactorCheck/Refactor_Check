public class zxing_0181 {

      private static BarcodeValue[][] createBarcodeMatrix(DetectionResult detectionResult) {
        BarcodeValue[][] barcodeMatrix =
            new BarcodeValue[detectionResult.getBarcodeRowCount()][detectionResult.getBarcodeColumnCount() + 2];
        for (int row = 0; row < barcodeMatrix.length; row++) {
          for (int column = 0; column < barcodeMatrix[row].length; column++) {
            barcodeMatrix[row][column] = new BarcodeValue();
          }
        }
    
        int column = 0;
        for (DetectionResultColumn detectionResultColumn : detectionResult.getDetectionResultColumns()) {
          if (detectionResultColumn != null) {
            for (Codeword codeword : detectionResultColumn.getCodewords()) {
              setBarcodeValue(barcodeMatrix, column, codeword);
            }
          }
          column++;
        }
        return barcodeMatrix;
      }

      private static void setBarcodeValue(BarcodeValue[][] barcodeMatrix, int column, Codeword codeword) {
        if (codeword != null) {
          int rowNumber = codeword.getRowNumber();
          if (rowNumber >= 0) {
            if (rowNumber >= barcodeMatrix.length) {
              // We have more rows than the barcode metadata allows for, ignore them.
              return;
            }
            barcodeMatrix[rowNumber][column].setValue(codeword.getValue());
          }
        }
      }
}
