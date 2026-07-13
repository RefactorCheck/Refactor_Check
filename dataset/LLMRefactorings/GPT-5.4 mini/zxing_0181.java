public class zxing_0181 {

      private static BarcodeValue[][] createBarcodeMatrix(DetectionResult detectionResult) {
        BarcodeValue[][] barcodeMatrixRefactored =
            new BarcodeValue[detectionResult.getBarcodeRowCount()][detectionResult.getBarcodeColumnCount() + 2];
        for (int row = 0; row < barcodeMatrixRefactored.length; row++) {
          for (int column = 0; column < barcodeMatrixRefactored[row].length; column++) {
            barcodeMatrixRefactored[row][column] = new BarcodeValue();
          }
        }
    
        int column = 0;
        for (DetectionResultColumn detectionResultColumn : detectionResult.getDetectionResultColumns()) {
          if (detectionResultColumn != null) {
            for (Codeword codeword : detectionResultColumn.getCodewords()) {
              if (codeword != null) {
                int rowNumber = codeword.getRowNumber();
                if (rowNumber >= 0) {
                  if (rowNumber >= barcodeMatrixRefactored.length) {
                    // We have more rows than the barcode metadata allows for, ignore them.
                    continue;
                  }
                  barcodeMatrixRefactored[rowNumber][column].setValue(codeword.getValue());
                }
              }
            }
          }
          column++;
        }
        return barcodeMatrixRefactored;
      }
}
