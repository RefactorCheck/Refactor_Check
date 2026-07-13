public class zxing_0074 {

      private ResultPoint[] centerEdges(ResultPoint y, ResultPoint z,
                                        ResultPoint x, ResultPoint t) {
        if (y.getX() < width / 2.0f) {
          return buildLeftEdges(y, z, x, t);
        } else {
          return buildRightEdges(y, z, x, t);
        }
      }

      private ResultPoint[] buildLeftEdges(ResultPoint y, ResultPoint z,
                                           ResultPoint x, ResultPoint t) {
        float yi = y.getX();
        float yj = y.getY();
        float zi = z.getX();
        float zj = z.getY();
        float xi = x.getX();
        float xj = x.getY();
        float ti = t.getX();
        float tj = t.getY();

        return new ResultPoint[]{
            new ResultPoint(ti - CORR, tj + CORR),
            new ResultPoint(zi + CORR, zj + CORR),
            new ResultPoint(xi - CORR, xj - CORR),
            new ResultPoint(yi + CORR, yj - CORR)};
      }

      private ResultPoint[] buildRightEdges(ResultPoint y, ResultPoint z,
                                            ResultPoint x, ResultPoint t) {
        float yi = y.getX();
        float yj = y.getY();
        float zi = z.getX();
        float zj = z.getY();
        float xi = x.getX();
        float xj = x.getY();
        float ti = t.getX();
        float tj = t.getY();

        return new ResultPoint[]{
            new ResultPoint(ti + CORR, tj + CORR),
            new ResultPoint(zi + CORR, zj - CORR),
            new ResultPoint(xi - CORR, xj + CORR),
            new ResultPoint(yi - CORR, yj - CORR)};
      }
}
