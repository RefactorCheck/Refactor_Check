public class zxing_0261 {

      BoundingBox addMissingRows(int missingStartRows, int missingEndRows, boolean isLeft) throws NotFoundException {
        ResultPoint newTopLeft = topLeft;
        ResultPoint newBottomLeft = bottomLeft;
        ResultPoint newTopRight = topRight;
        ResultPoint newBottomRight = bottomRight;
    
        if (missingStartRows > 0) {
          ResultPoint top = isLeft ? topLeft : topRight;
          int newMinY = (int) top.getY() - missingStartRows;
          if (newMinY < 0) {
            newMinY = 0;
          }
          ResultPoint newTop = new ResultPoint(top.getX(), newMinY);
          if (isLeft) {
            newTopLeft = newTop;
          } else {
            newTopRight = newTop;
          }
        }
    
        if (missingEndRows > 0) {
          ResultPoint bottom = isLeft ? bottomLeft : bottomRight;
          int newMaxY = (int) bottom.getY() + missingEndRows;
          if (newMaxY >= image.getHeight()) {
            newMaxY = image.getHeight() - 1;
          }
          ResultPoint newBottom = new ResultPoint(bottom.getX(), newMaxY);
          if (isLeft) {
            newBottomLeft = newBottom;
          } else {
            newBottomRight = newBottom;
          }
        }
    
        return new BoundingBox(image, newTopLeft, newBottomLeft, newTopRight, newBottomRight);
      }
}
