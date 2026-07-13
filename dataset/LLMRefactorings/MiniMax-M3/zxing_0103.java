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
    
        if (isMetadataMismatch(leftBarcodeMetadata, rightBarcodeMetadata)) {
          return null;
        }
        return leftBarcodeMetadata;
      }

      private static boolean isMetadataMismatch(BarcodeMetadata left, BarcodeMetadata right) {
        return left.getColumnCount() != right.getColumnCount() &&
               left.getErrorCorrectionLevel() != right.getErrorCorrectionLevel() &&
               left.getRowCount() != right.getRowCount();
      }
}
