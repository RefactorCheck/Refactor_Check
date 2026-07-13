public class zxing_0044 {

      private ResultPoint correctTopRight(ResultPoint[] points) {
        // A..D
        // |  :
        // B--C
        ResultPoint pointA = points[0];
        ResultPoint pointB = points[1];
        ResultPoint pointC = points[2];
        ResultPoint pointD = points[3];
    
        // shift points for safe transition detection.
        int trTop = transitionsBetween(pointA, pointD);
        int trRight = transitionsBetween(pointB, pointD);
        ResultPoint pointAs = shiftPoint(pointA, pointB, (trRight + 1) * 4);
        ResultPoint pointCs = shiftPoint(pointC, pointB, (trTop + 1) * 4);
    
        trTop = transitionsBetween(pointAs, pointD);
        trRight = transitionsBetween(pointCs, pointD);
    
        ResultPoint candidate1 = new ResultPoint(
            pointD.getX() + (pointC.getX() - pointB.getX()) / (trTop + 1),
            pointD.getY() + (pointC.getY() - pointB.getY()) / (trTop + 1));
        ResultPoint candidate2 = new ResultPoint(
            pointD.getX() + (pointA.getX() - pointB.getX()) / (trRight + 1),
            pointD.getY() + (pointA.getY() - pointB.getY()) / (trRight + 1));
    
        if (!isValid(candidate1)) {
          if (isValid(candidate2)) {
            return candidate2;
          }
          return null;
        }
        if (!isValid(candidate2)) {
          return candidate1;
        }
    
        int sumc1 = transitionsBetween(pointAs, candidate1) + transitionsBetween(pointCs, candidate1);
        int sumc2 = transitionsBetween(pointAs, candidate2) + transitionsBetween(pointCs, candidate2);
    
        if (sumc1 > sumc2) {
          return candidate1;
        } else {
          return candidate2;
        }
      }
}
