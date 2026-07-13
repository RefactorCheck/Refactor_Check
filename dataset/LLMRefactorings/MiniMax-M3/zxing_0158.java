public class zxing_0158 {

      public int decode(int[] received,
                        int numECCodewords,
                        int[] erasures) throws ChecksumException {
    
        if (received.length > field.getSize()) {
          // A codeword cannot be longer than the field; otherwise erasure and error positions
          // index past the exponent table in exp()/log().
          throw ChecksumException.getChecksumInstance();
        }
    
        ModulusPoly poly = new ModulusPoly(field, received);
        int[] S = new int[numECCodewords];
        boolean error = false;
        for (int i = numECCodewords; i > 0; i--) {
          int eval = poly.evaluateAt(field.exp(i));
          S[numECCodewords - i] = eval;
          if (eval != 0) {
            error = true;
          }
        }
    
        if (!error) {
          return 0;
        }
    
        ModulusPoly knownErrors = buildKnownErrors(erasures, received);
    
        ModulusPoly syndrome = new ModulusPoly(field, S);
    
        ModulusPoly[] sigmaOmega =
            runEuclideanAlgorithm(field.buildMonomial(numECCodewords, 1), syndrome, numECCodewords);
        ModulusPoly sigma = sigmaOmega[0];
        ModulusPoly omega = sigmaOmega[1];
    
        int[] errorLocations = findErrorLocations(sigma);
        int[] errorMagnitudes = findErrorMagnitudes(omega, sigma, errorLocations);
    
        for (int i = 0; i < errorLocations.length; i++) {
          int position = received.length - 1 - field.log(errorLocations[i]);
          if (position < 0) {
            throw ChecksumException.getChecksumInstance();
          }
          received[position] = field.subtract(received[position], errorMagnitudes[i]);
        }
        return errorLocations.length;
      }
    
      private ModulusPoly buildKnownErrors(int[] erasures, int[] received) {
        ModulusPoly knownErrors = field.getOne();
        if (erasures != null) {
          for (int erasure : erasures) {
            int b = field.exp(received.length - 1 - erasure);
            // Add (1 - bx) term:
            ModulusPoly term = new ModulusPoly(field, new int[]{field.subtract(0, b), 1});
            knownErrors = knownErrors.multiply(term);
          }
        }
        return knownErrors;
      }
}
