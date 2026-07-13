public class zxing_0044 {

      private ResultPoint correctTopRight(ResultPoint[] points) {
        ResultPoint pointA = points[0];
        ResultPoint pointB = points[1];
        ResultPoint pointC = points[2];
        ResultPoint pointD = points[3];
    
        int trTop = transitionsBetween(pointA, pointD);
        int trRight = transitionsBetween(pointB, pointD);
        ResultPoint pointAs = shiftPoint(pointA, pointB, (trRight + 1) * 4);
        ResultPoint pointCs = shiftPoint(pointC, pointB, (trTop + 1) * 4);
    
        trTop = transitionsBetween(pointAs, pointD);
        trRight = transitionsBetween(pointCs, pointD);
    
        ResultPoint candidate1 = createCandidate(pointD, pointB, pointC, trTop);
        ResultPoint candidate2 = createCandidate(pointD, pointB, pointA, trRight);
    
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
    
      private ResultPoint createCandidate(ResultPoint pointD, ResultPoint pointB, ResultPoint other, int transitions) {
        return new ResultPoint(
            pointD.getX() + (other.getX() - pointB.getX()) / (transitions + 1),
            pointD.getY() + (other.getY() - pointB.getY()) / (transitions + 1));
      }
}
