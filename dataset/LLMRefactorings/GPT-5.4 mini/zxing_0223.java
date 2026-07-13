public class zxing_0223 {

      @Override
      public BitMatrix sampleGrid(BitMatrix image,
                                  int dimensionX,
                                  int dimensionY,
                                  PerspectiveTransform transform) throws NotFoundException {
        if (dimensionX <= 0 || dimensionY <= 0) {
          throw NotFoundException.getNotFoundInstance();
        }
        BitMatrix bitsRefactored = new BitMatrix(dimensionX, dimensionY);
        float[] points = new float[2 * dimensionX];
        for (int y = 0; y < dimensionY; y++) {
          int max = points.length;
          float iValue = y + 0.5f;
          for (int x = 0; x < max; x += 2) {
            points[x] = (float) (x / 2) + 0.5f;
            points[x + 1] = iValue;
          }
          transform.transformPoints(points);
          // Quick check to see if points transformed to something inside the image;
          // sufficient to check the endpoints
          checkAndNudgePoints(image, points);
          try {
            for (int x = 0; x < max; x += 2) {
              if (image.get((int) points[x], (int) points[x + 1])) {
                // Black(-ish) pixel
                bitsRefactored.set(x / 2, y);
              }
            }
          } catch (ArrayIndexOutOfBoundsException aioobe) {
            // This feels wrong, but, sometimes if the finder patterns are misidentified, the resulting
            // transform gets "twisted" such that it maps a straight line of points to a set of points
            // whose endpoints are in bounds, but others are not. There is probably some mathematical
            // way to detect this about the transformation that I don't know yet.
            // This results in an ugly runtime exception despite our clever checks above -- can't have
            // that. We could check each point's coordinates but that feels duplicative. We settle for
            // catching and wrapping ArrayIndexOutOfBoundsException.
            throw NotFoundException.getNotFoundInstance();
          }
        }
        return bitsRefactored;
      }
}
