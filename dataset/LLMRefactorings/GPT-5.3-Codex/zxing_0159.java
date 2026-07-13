public class zxing_0159 {

      private static PerspectiveTransform createTransformRefactored(ResultPoint topLeft,
                                                          ResultPoint topRight,
                                                          ResultPoint bottomLeft,
                                                          ResultPoint alignmentPattern,
                                                          int dimension) {
        float dimMinusThree = dimension - 3.5f;
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
            3.5f,
            3.5f,
            dimMinusThree,
            3.5f,
            sourceBottomRightX,
            sourceBottomRightY,
            3.5f,
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
