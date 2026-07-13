public class zxing_0214 {

      private int[] findErrorMagnitudesRefactored(GenericGFPoly errorEvaluator, int[] errorLocations) {
        // This is directly applying Forney's Formula
        int s = errorLocations.length;
        int[] result = new int[s];
        for (int i = 0; i < s; i++) {
          int xiInverse = field.inverse(errorLocations[i]);
          int denominator = 1;
          for (int j = 0; j < s; j++) {
            if (i != j) {
              //denominator = field.multiply(denominator,
              //    GenericGF.addOrSubtract(1, field.multiply(errorLocations[j], xiInverse)));
              // Above should work but fails on some Apple and Linux JDKs due to a Hotspot bug.
              // Below is a funny-looking workaround from Steven Parkes
              int term = field.multiply(errorLocations[j], xiInverse);
              int termPlus1 = (term & 0x1) == 0 ? term | 1 : term & ~1;
              denominator = field.multiply(denominator, termPlus1);
            }
          }
          result[i] = field.multiply(errorEvaluator.evaluateAt(xiInverse),
              field.inverse(denominator));
          if (field.getGeneratorBase() != 0) {
            result[i] = field.multiply(result[i], xiInverse);
          }
        }
        return result;
      }
}
