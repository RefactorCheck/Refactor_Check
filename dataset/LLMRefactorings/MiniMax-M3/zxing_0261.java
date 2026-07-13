public class zxing_0261 {

      BoundingBox addMissingRows(int missingStartRows, int missingEndRows, boolean isLeft) throws NotFoundException {
        ResultPoint newTopLeft = topLeft;
        ResultPoint newBottomLeft = bottomLeft;
        ResultPoint newTopRight = topRight;
        ResultPoint newBottomRight = bottomRight;
        int maxY = image.getHeight() - 1;

        if (missingStartRows > 0) {
          ResultPoint top = isLeft ? topLeft : topRight;
          ResultPoint newTop = shiftPoint(top, -missingStartRows, 0, maxY);
          if (isLeft) {
            newTopLeft = newTop;
          } else {
            newTopRight = newTop;
          }
        }

        if (missingEndRows > 0) {
          ResultPoint bottom = isLeft ? bottomLeft : bottomRight;
          ResultPoint newBottom = shiftPoint(bottom, missingEndRows, 0, maxY);
          if (isLeft) {
            newBottomLeft = newBottom;
          } else {
            newBottomRight = newBottom;
          }
        }

        return new BoundingBox(image, newTopLeft, newBottomLeft, newTopRight, newBottomRight);
      }

      private ResultPoint shiftPoint(ResultPoint point, int yShift, int lowerBound, int upperBound) {
        int newY = (int) point.getY() + yShift;
        if (newY < lowerBound) {
          newY = lowerBound;
        }
        if (newY > upperBound) {
          newY = upperBound;
        }
        return new ResultPoint(point.getX(), newY);
      }
}
