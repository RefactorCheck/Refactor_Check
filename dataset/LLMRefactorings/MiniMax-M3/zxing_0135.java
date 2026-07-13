public class zxing_0135 {

      private static BitMatrix bitMatrixFromEncoder(PDF417 encoder,
                                                    String contents,
                                                    int errorCorrectionLevel,
                                                    int width,
                                                    int height,
                                                    int margin,
                                                    boolean autoECI) throws WriterException {
        encoder.generateBarcodeLogic(contents, errorCorrectionLevel, autoECI);
    
        int aspectRatio = 4;
        byte[][] originalScale = encoder.getBarcodeMatrix().getScaledMatrix(1, aspectRatio);
        boolean rotated = shouldRotate(originalScale, height, width);
        if (rotated) {
          originalScale = rotateArray(originalScale);
        }
    
        int scaleX = width / originalScale[0].length;
        int scaleY = height / originalScale.length;
        int scale = Math.min(scaleX, scaleY);
    
        if (scale > 1) {
          byte[][] scaledMatrix =
              encoder.getBarcodeMatrix().getScaledMatrix(scale, scale * aspectRatio);
          if (rotated) {
            scaledMatrix = rotateArray(scaledMatrix);
          }
          return bitMatrixFromBitArray(scaledMatrix, margin);
        }
        return bitMatrixFromBitArray(originalScale, margin);
      }
    
      private static boolean shouldRotate(byte[][] matrix, int height, int width) {
        return (height > width) != (matrix[0].length < matrix.length);
      }
}
