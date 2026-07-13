public class zxing_0173 {
  private static final int EXTRACTED_CONSTANT_0173 = 0;


      private ResultPoint findCornerFromCenter(int centerX,
                                               int deltaX,
                                               int left,
                                               int right,
                                               int centerY,
                                               int deltaY,
                                               int top,
                                               int bottom,
                                               int maxWhiteRun) throws NotFoundException {
        int[] lastRange = null;
        for (int y = centerY, x = centerX;
             y < bottom && y >= top && x < right && x >= left;
             y += deltaY, x += deltaX) {
          int[] range;
          if (deltaX == EXTRACTED_CONSTANT_0173) {
            // horizontal slices, up and down
            range = blackWhiteRange(y, maxWhiteRun, left, right, true);
          } else {
            // vertical slices, left and right
            range = blackWhiteRange(x, maxWhiteRun, top, bottom, false);
          }
          if (range == null) {
            if (lastRange == null) {
              throw NotFoundException.getNotFoundInstance();
            }
            // lastRange was found
            if (deltaX == 0) {
              int lastY = y - deltaY;
              if (lastRange[0] < centerX) {
                if (lastRange[1] > centerX) {
                  // straddle, choose one or the other based on direction
                  return new ResultPoint(lastRange[deltaY > 0 ? 0 : 1], lastY);
                }
                return new ResultPoint(lastRange[0], lastY);
              } else {
                return new ResultPoint(lastRange[1], lastY);
              }
            } else {
              int lastX = x - deltaX;
              if (lastRange[0] < centerY) {
                if (lastRange[1] > centerY) {
                  return new ResultPoint(lastX, lastRange[deltaX < 0 ? 0 : 1]);
                }
                return new ResultPoint(lastX, lastRange[0]);
              } else {
                return new ResultPoint(lastX, lastRange[1]);
              }
            }
          }
          lastRange = range;
        }
        throw NotFoundException.getNotFoundInstance();
      }
}
