public class zxing_0242 {

      private ModulusPoly[] runEuclideanAlgorithm(ModulusPoly a, ModulusPoly b, int R)
          throws ChecksumException {
        // Assume a's degree is >= b's
        if (a.getDegree() < b.getDegree()) {
          ModulusPoly temp = a;
          a = b;
          b = temp;
        }
    
        ModulusPoly rLast = a;
        ModulusPoly r = b;
        ModulusPoly tLast = field.getZero();
        ModulusPoly t = field.getOne();
    
        // Run Euclidean algorithm until r's degree is less than R/2
        while (r.getDegree() >= R / 2) {
          ModulusPoly rLastLast = rLast;
          ModulusPoly tLastLast = tLast;
          rLast = r;
          tLast = t;
    
          if (rLast.isZero()) {
            // Oops, Euclidean algorithm already terminated?
            throw ChecksumException.getChecksumInstance();
          }
          ModulusPoly[] qr = performPolynomialDivision(rLastLast, rLast);
          ModulusPoly q = qr[0];
          r = qr[1];
    
          t = q.multiply(tLast).subtract(tLastLast).negative();
        }
    
        int sigmaTildeAtZero = t.getCoefficient(0);
        if (sigmaTildeAtZero == 0) {
          throw ChecksumException.getChecksumInstance();
        }
    
        int inverse = field.inverse(sigmaTildeAtZero);
        ModulusPoly sigma = t.multiply(inverse);
        ModulusPoly omega = r.multiply(inverse);
        return new ModulusPoly[]{sigma, omega};
      }
    
      private ModulusPoly[] performPolynomialDivision(ModulusPoly dividend, ModulusPoly divisor) {
        ModulusPoly q = field.getZero();
        int denominatorLeadingTerm = divisor.getCoefficient(divisor.getDegree());
        int dltInverse = field.inverse(denominatorLeadingTerm);
        ModulusPoly r = dividend;
        while (r.getDegree() >= divisor.getDegree() && !r.isZero()) {
          int degreeDiff = r.getDegree() - divisor.getDegree();
          int scale = field.multiply(r.getCoefficient(r.getDegree()), dltInverse);
          q = q.add(field.buildMonomial(degreeDiff, scale));
          r = r.subtract(divisor.multiplyByMonomial(degreeDiff, scale));
        }
        return new ModulusPoly[]{q, r};
      }
}
