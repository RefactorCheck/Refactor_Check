public class zxing_0270 {

  private GenericGFPoly[] runEuclideanAlgorithm(GenericGFPoly a, GenericGFPoly b, int R)
      throws ReedSolomonException {
    // Assume a's degree is >= b's
    if (a.getDegree() < b.getDegree()) {
      GenericGFPoly temp = a;
      a = b;
      b = temp;
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
      int dltInverse = field.inverse(rLast.getCoefficient(rLast.getDegree()));
      GenericGFPoly[] qr = divide(rLastLast, rLast, dltInverse);
      r = qr[0];
      GenericGFPoly q = qr[1];

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

  private GenericGFPoly[] divide(GenericGFPoly dividend, GenericGFPoly divisor, int dltInverse) {
    GenericGFPoly remainder = dividend;
    GenericGFPoly quotient = field.getZero();
    while (remainder.getDegree() >= divisor.getDegree() && !remainder.isZero()) {
      int degreeDiff = remainder.getDegree() - divisor.getDegree();
      int scale = field.multiply(remainder.getCoefficient(remainder.getDegree()), dltInverse);
      quotient = quotient.addOrSubtract(field.buildMonomial(degreeDiff, scale));
      remainder = remainder.addOrSubtract(divisor.multiplyByMonomial(degreeDiff, scale));
    }
    return new GenericGFPoly[]{remainder, quotient};
  }
}
