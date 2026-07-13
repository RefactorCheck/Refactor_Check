public class zxing_0199 {

      public static DetectorResult detect() throws NotFoundException {
    
        ResultPoint[] cornerPoints = rectangleDetector.detect();
    
        ResultPoint[] points = detectSolid1(cornerPoints);
        points = detectSolid2(points);
        points[3] = correctTopRight(points);
        if (points[3] == null) {
          throw NotFoundException.getNotFoundInstance();
        }
        points = shiftToModuleCenter(points);
    
        ResultPoint topLeft = points[0];
        ResultPoint bottomLeft = points[1];
        ResultPoint bottomRight = points[2];
        ResultPoint topRight = points[3];
    
        int dimensionTop = transitionsBetween(topLeft, topRight) + 1;
        int dimensionRight = transitionsBetween(bottomRight, topRight) + 1;
        if ((dimensionTop & 0x01) == 1) {
          dimensionTop += 1;
        }
        if ((dimensionRight & 0x01) == 1) {
          dimensionRight += 1;
        }
    
        if (4 * dimensionTop < 6 * dimensionRight && 4 * dimensionRight < 6 * dimensionTop) {
          // The matrix is square
          dimensionTop = dimensionRight = Math.max(dimensionTop, dimensionRight);
        }
    
        BitMatrix bits = sampleGrid(image,
                                    topLeft,
                                    bottomLeft,
                                    bottomRight,
                                    topRight,
                                    dimensionTop,
                                    dimensionRight);
    
        return new DetectorResult(bits, new ResultPoint[]{topLeft, bottomLeft, bottomRight, topRight});
      }
}
