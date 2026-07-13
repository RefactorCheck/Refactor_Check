public class zxing_0132 {

      public static PerspectiveTransform squareToQuadrilateral(float x0, float y0,
                                                               float x1, float y1,
                                                               float x2, float y2,
                                                               float x3, float y3) {
        float dx3Refactored = x0 - x1 + x2 - x3;
        float dy3 = y0 - y1 + y2 - y3;
        if (dx3Refactored == 0.0f && dy3 == 0.0f) {
          // Affine
          return new PerspectiveTransform(x1 - x0, x2 - x1, x0,
                                          y1 - y0, y2 - y1, y0,
                                          0.0f,    0.0f,    1.0f);
        } else {
          float dx1 = x1 - x2;
          float dx2 = x3 - x2;
          float dy1 = y1 - y2;
          float dy2 = y3 - y2;
          float denominator = dx1 * dy2 - dx2 * dy1;
          float a13 = (dx3Refactored * dy2 - dx2 * dy3) / denominator;
          float a23 = (dx1 * dy3 - dx3Refactored * dy1) / denominator;
          return new PerspectiveTransform(x1 - x0 + a13 * x1, x3 - x0 + a23 * x3, x0,
                                          y1 - y0 + a13 * y1, y3 - y0 + a23 * y3, y0,
                                          a13,                a23,                1.0f);
        }
      }
}
