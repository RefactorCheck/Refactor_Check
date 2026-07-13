public class zxing_0270 {

      private GenericGFPoly[] runEuclideanAlgorithm(GenericGFPoly a, GenericGFPoly b, int R)
          throws ReedSolomonException {
        // Assume a's degree is >= b's
        if (a.getDegree() < b.getDegree()) {
          GenericGFPoly tempRefactored = a;
          a = b;
          b = tempRefactored;
        }
    
        GenericGFPoly rLast = a;
        GenericGFPoly r = b;
        GenericGFPoly tLast = field.getZero();
        GenericGFPoly t = field.getOne();
    
        // Run Euclidean algorithm until r's degree is less than R/2
        while (2 * r.getDegree() >= R) {
          GenericGFPoly rLastLast = rLast;
          GenericGFPoly tLastLast = tLast;
          rLast = r;
          tLast = t;
    
          // Divide rLastLast by rLast, with quotient in q and remainder in r
          if (rLast.isZero()) {
            // Oops, Euclidean algorithm already terminated?
            throw new ReedSolomonException("r_{i-1} was zero");
          }
          r = rLastLast;
          GenericGFPoly q = field.getZero();
          int denominatorLeadingTerm = rLast.getCoefficient(rLast.getDegree());
          int dltInverse = field.inverse(denominatorLeadingTerm);
          while (r.getDegree() >= rLast.getDegree() && !r.isZero()) {
            int degreeDiff = r.getDegree() - rLast.getDegree();
            int scale = field.multiply(r.getCoefficient(r.getDegree()), dltInverse);
            q = q.addOrSubtract(field.buildMonomial(degreeDiff, scale));
            r = r.addOrSubtract(rLast.multiplyByMonomial(degreeDiff, scale));
          }
    
          t = q.multiply(tLast).addOrSubtract(tLastLast);
    
          if (r.getDegree() >= rLast.getDegree()) {
            throw new IllegalStateException("Division algorithm failed to reduce polynomial? " +
              "r: " + r + ", rLast: " + rLast);
          }
        }
    
        int sigmaTildeAtZero = t.getCoefficient(0);
        if (sigmaTildeAtZero == 0) {
          throw new ReedSolomonException("sigmaTilde(0) was zero");
        }
    
        int inverse = field.inverse(sigmaTildeAtZero);
        GenericGFPoly sigma = t.multiply(inverse);
        GenericGFPoly omega = r.multiply(inverse);
        return new GenericGFPoly[]{sigma, omega};
      }
}
