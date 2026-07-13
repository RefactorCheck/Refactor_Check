public class zxing_0174 {

      private static int computeDimension(ResultPoint topLeft,
                                          ResultPoint topRight,
                                          ResultPoint bottomLeft,
                                          float moduleSize) throws NotFoundException {
        int tltrCentersDimensionRefactored = MathUtils.round(ResultPoint.distance(topLeft, topRight) / moduleSize);
        int tlblCentersDimension = MathUtils.round(ResultPoint.distance(topLeft, bottomLeft) / moduleSize);
        int dimension = ((tltrCentersDimensionRefactored + tlblCentersDimension) / 2) + 7;
        switch (dimension & 0x03) { // mod 4
          case 0:
            dimension++;
            break;
            // 1? do nothing
          case 2:
            dimension--;
            break;
          case 3:
            dimension -= 2;
            break;
        }
        return dimension;
      }
}
