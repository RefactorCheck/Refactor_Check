public class zxing_0159 {

      private static final float MODULE_CENTER_OFFSET = 3.5f;

      private static PerspectiveTransform createTransform(ResultPoint topLeft,
                                                          ResultPoint topRight,
                                                          ResultPoint bottomLeft,
                                                          ResultPoint alignmentPattern,
                                                          int dimension) {
        float dimMinusThree = dimension - MODULE_CENTER_OFFSET;
        float bottomRightX;
        float bottomRightY;
        float sourceBottomRightX;
        float sourceBottomRightY;
        if (alignmentPattern != null) {
          bottomRightX = alignmentPattern.getX();
          bottomRightY = alignmentPattern.getY();
          sourceBottomRightX = dimMinusThree - 3.0f;
          sourceBottomRightY = sourceBottomRightX;
        } else {
          // Don't have an alignment pattern, just make up the bottom-right point
          bottomRightX = (topRight.getX() - topLeft.getX()) + bottomLeft.getX();
          bottomRightY = (topRight.getY() - topLeft.getY()) + bottomLeft.getY();
          sourceBottomRightX = dimMinusThree;
          sourceBottomRightY = dimMinusThree;
        }
    
        return PerspectiveTransform.quadrilateralToQuadrilateral(
            MODULE_CENTER_OFFSET,
            MODULE_CENTER_OFFSET,
            dimMinusThree,
            MODULE_CENTER_OFFSET,
            sourceBottomRightX,
            sourceBottomRightY,
            MODULE_CENTER_OFFSET,
            dimMinusThree,
            topLeft.getX(),
            topLeft.getY(),
            topRight.getX(),
            topRight.getY(),
            bottomRightX,
            bottomRightY,
            bottomLeft.getX(),
            bottomLeft.getY());
      }
}
