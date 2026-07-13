public class zxing_0103 {

      private static BarcodeMetadata getBarcodeMetadata(DetectionResultRowIndicatorColumn leftRowIndicatorColumn,
                                                        DetectionResultRowIndicatorColumn rightRowIndicatorColumn) {
        BarcodeMetadata leftBarcodeMetadata;
        if (leftRowIndicatorColumn == null ||
            (leftBarcodeMetadata = leftRowIndicatorColumn.getBarcodeMetadata()) == null) {
          return rightRowIndicatorColumn == null ? null : rightRowIndicatorColumn.getBarcodeMetadata();
        }
        BarcodeMetadata rightBarcodeMetadata;
        if (rightRowIndicatorColumn == null ||
            (rightBarcodeMetadata = rightRowIndicatorColumn.getBarcodeMetadata()) == null) {
          return leftBarcodeMetadata;
        }
    
        if (leftBarcodeMetadata.getColumnCount() != rightBarcodeMetadata.getColumnCount() &&
            leftBarcodeMetadata.getErrorCorrectionLevel() != rightBarcodeMetadata.getErrorCorrectionLevel() &&
            leftBarcodeMetadata.getRowCount() != rightBarcodeMetadata.getRowCount()) {
          return null;
        }
        return leftBarcodeMetadata;
      }
}
