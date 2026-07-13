public class zxing_0174 {

      private static final int DIMENSION_OFFSET = 7;
      private static final int MOD_4_MASK = 0x03;

      private static int computeDimension(ResultPoint topLeft,
                                          ResultPoint topRight,
                                          ResultPoint bottomLeft,
                                          float moduleSize) throws NotFoundException {
        int tltrCentersDimension = MathUtils.round(ResultPoint.distance(topLeft, topRight) / moduleSize);
        int tlblCentersDimension = MathUtils.round(ResultPoint.distance(topLeft, bottomLeft) / moduleSize);
        int dimension = ((tltrCentersDimension + tlblCentersDimension) / 2) + DIMENSION_OFFSET;
        switch (dimension & MOD_4_MASK) { // mod 4
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
