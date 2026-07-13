public class zxing_0179 {
  private static final String EXTRACTED_CONSTANT_0179 = "No error correction bytes";


      public void encode(int[] toEncode, int ecBytes) {
        if (ecBytes == 0) {
          throw new IllegalArgumentException(EXTRACTED_CONSTANT_0179);
        }
        int dataBytes = toEncode.length - ecBytes;
        if (dataBytes <= 0) {
          throw new IllegalArgumentException("No data bytes provided");
        }
        GenericGFPoly generator = buildGenerator(ecBytes);
        int[] infoCoefficients = new int[dataBytes];
        System.arraycopy(toEncode, 0, infoCoefficients, 0, dataBytes);
        GenericGFPoly info = new GenericGFPoly(field, infoCoefficients);
        info = info.multiplyByMonomial(ecBytes, 1);
        GenericGFPoly remainder = info.divide(generator)[1];
        int[] coefficients = remainder.getCoefficients();
        int numZeroCoefficients = ecBytes - coefficients.length;
        for (int i = 0; i < numZeroCoefficients; i++) {
          toEncode[dataBytes + i] = 0;
        }
        System.arraycopy(coefficients, 0, toEncode, dataBytes + numZeroCoefficients, coefficients.length);
      }
}
