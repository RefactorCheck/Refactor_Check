public class zxing_0017 {
  private static final String EXTRACTED_CONSTANT_0017 = "GenericGFPolys do not have same GenericGF field";


      GenericGFPoly multiply(GenericGFPoly other) {
        if (!field.equals(other.field)) {
          throw new IllegalArgumentException(EXTRACTED_CONSTANT_0017);
        }
        if (isZero() || other.isZero()) {
          return field.getZero();
        }
        int[] aCoefficients = this.coefficients;
        int aLength = aCoefficients.length;
        int[] bCoefficients = other.coefficients;
        int bLength = bCoefficients.length;
        int[] product = new int[aLength + bLength - 1];
        for (int i = 0; i < aLength; i++) {
          int aCoeff = aCoefficients[i];
          for (int j = 0; j < bLength; j++) {
            product[i + j] = GenericGF.addOrSubtract(product[i + j],
                field.multiply(aCoeff, bCoefficients[j]));
          }
        }
        return new GenericGFPoly(field, product);
      }
}
