public class zxing_0095 {

      private ResultPoint[] detectSolid1(ResultPoint[] cornerPoints) {
        // 0  2
        // 1  3
        ResultPoint pointA = cornerPoints[0];
        ResultPoint pointB = cornerPoints[1];
        ResultPoint pointC = cornerPoints[3];
        ResultPoint pointD = cornerPoints[2];
    
        int trAB = transitionsBetween(pointA, pointB);
        int trBC = transitionsBetween(pointB, pointC);
        int trCD = transitionsBetween(pointC, pointD);
        int trDA = transitionsBetween(pointD, pointA);
    
        // 0..3
        // :  :
        // 1--2
        int min = trAB;
        ResultPoint[] points = {pointD, pointA, pointB, pointC};
        if (min > trBC) {
          min = trBC;
          points[0] = pointA;
          points[1] = pointB;
          points[2] = pointC;
          points[3] = pointD;
        }
        if (min > trCD) {
          min = trCD;
          points[0] = pointB;
          points[1] = pointC;
          points[2] = pointD;
          points[3] = pointA;
        }
        if (min > trDA) {
          points[0] = pointC;
          points[1] = pointD;
          points[2] = pointA;
          points[3] = pointB;
        }
    
        return points;
      }
}
