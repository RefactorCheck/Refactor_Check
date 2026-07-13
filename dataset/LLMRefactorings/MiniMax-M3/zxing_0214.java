public class zxing_0214 {

      private int[] findErrorMagnitudes(GenericGFPoly errorEvaluator, int[] errorLocations) {
        // This is directly applying Forney's Formula
        int s = errorLocations.length;
        int[] result = new int[s];
        for (int i = 0; i < s; i++) {
          int xiInverse = field.inverse(errorLocations[i]);
          int denominator = calculateDenominator(errorLocations, xiInverse, i, s);
          result[i] = field.multiply(errorEvaluator.evaluateAt(xiInverse),
              field.inverse(denominator));
          if (field.getGeneratorBase() != 0) {
            result[i] = field.multiply(result[i], xiInverse);
          }
        }
        return result;
      }
      
      private int calculateDenominator(int[] errorLocations, int xiInverse, int i, int s) {
        int denominator = 1;
        for (int j = 0; j < s; j++) {
          if (i != j) {
            int term = field.multiply(errorLocations[j], xiInverse);
            int termPlus1 = (term & 0x1) == 0 ? term | 1 : term & ~1;
            denominator = field.multiply(denominator, termPlus1);
          }
        }
        return denominator;
      }
}
