public class zxing_0002 {

      public int decodeWithECCount(int[] received, int twoS) throws ReedSolomonException {
        GenericGFPoly poly = new GenericGFPoly(field, received);
        int[] syndromeCoefficients = new int[twoS];
        boolean noError = true;
        for (int i = 0; i < twoS; i++) {
          int eval = poly.evaluateAt(field.exp(i + field.getGeneratorBase()));
          syndromeCoefficients[syndromeCoefficients.length - 1 - i] = eval;
          if (eval != 0) {
            noError = false;
          }
        }
        if (noError) {
          return 0;
        }
        GenericGFPoly syndrome = new GenericGFPoly(field, syndromeCoefficients);
        GenericGFPoly[] sigmaOmega =
            runEuclideanAlgorithm(field.buildMonomial(twoS, 1), syndrome, twoS);
        GenericGFPoly sigma = sigmaOmega[0];
        GenericGFPoly omega = sigmaOmega[1];
        int[] errorLocations = findErrorLocations(sigma);
        int[] errorMagnitudes = findErrorMagnitudes(omega, errorLocations);
        applyErrors(received, errorLocations, errorMagnitudes);
        return errorLocations.length;
      }

      private void applyErrors(int[] received, int[] errorLocations, int[] errorMagnitudes) throws ReedSolomonException {
        for (int i = 0; i < errorLocations.length; i++) {
          int position = received.length - 1 - field.log(errorLocations[i]);
          if (position < 0) {
            throw new ReedSolomonException("Bad error location");
          }
          received[position] = GenericGF.addOrSubtract(received[position], errorMagnitudes[i]);
        }
      }
}
