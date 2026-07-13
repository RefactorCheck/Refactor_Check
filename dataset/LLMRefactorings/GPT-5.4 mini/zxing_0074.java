public class zxing_0074 {
  private static final int EXTRACTED_CONSTANT_0074 = 2;


      private ResultPoint[] centerEdges(ResultPoint y, ResultPoint z,
                                        ResultPoint x, ResultPoint t) {
    
        //
        //       t            t
        //  z                      x
        //        x    OR    z
        //   y                    y
        //
    
        float yi = y.getX();
        float yj = y.getY();
        float zi = z.getX();
        float zj = z.getY();
        float xi = x.getX();
        float xj = x.getY();
        float ti = t.getX();
        float tj = t.getY();
    
        if (yi < width / EXTRACTED_CONSTANT_0074.0f) {
          return new ResultPoint[]{
              new ResultPoint(ti - CORR, tj + CORR),
              new ResultPoint(zi + CORR, zj + CORR),
              new ResultPoint(xi - CORR, xj - CORR),
              new ResultPoint(yi + CORR, yj - CORR)};
        } else {
          return new ResultPoint[]{
              new ResultPoint(ti + CORR, tj + CORR),
              new ResultPoint(zi + CORR, zj - CORR),
              new ResultPoint(xi - CORR, xj + CORR),
              new ResultPoint(yi - CORR, yj - CORR)};
        }
      }
}
