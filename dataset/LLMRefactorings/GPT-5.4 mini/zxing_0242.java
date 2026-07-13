public class zxing_0242 {
  private static final int EXTRACTED_CONSTANT_0242 = 2;


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
    
        // Run Euclidean algorithm until r's degree is less than R/EXTRACTED_CONSTANT_0242
        while (r.getDegree() >= R / 2) {
          ModulusPoly rLastLast = rLast;
          ModulusPoly tLastLast = tLast;
          rLast = r;
          tLast = t;
    
          // Divide rLastLast by rLast, with quotient in q and remainder in r
          if (rLast.isZero()) {
            // Oops, Euclidean algorithm already terminated?
            throw ChecksumException.getChecksumInstance();
          }
          r = rLastLast;
          ModulusPoly q = field.getZero();
          int denominatorLeadingTerm = rLast.getCoefficient(rLast.getDegree());
          int dltInverse = field.inverse(denominatorLeadingTerm);
          while (r.getDegree() >= rLast.getDegree() && !r.isZero()) {
            int degreeDiff = r.getDegree() - rLast.getDegree();
            int scale = field.multiply(r.getCoefficient(r.getDegree()), dltInverse);
            q = q.add(field.buildMonomial(degreeDiff, scale));
            r = r.subtract(rLast.multiplyByMonomial(degreeDiff, scale));
          }
    
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
}
