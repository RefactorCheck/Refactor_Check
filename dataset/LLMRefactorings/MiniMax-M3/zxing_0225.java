public class zxing_0225 {

      int evaluateAt(int a) {
        if (a == 0) {
          return getCoefficient(0);
        }
        if (a == 1) {
          return sumCoefficients();
        }
        int result = coefficients[0];
        int size = coefficients.length;
        for (int i = 1; i < size; i++) {
          result = GenericGF.addOrSubtract(field.multiply(a, result), coefficients[i]);
        }
        return result;
      }
      
      private int sumCoefficients() {
        int result = 0;
        for (int coefficient : coefficients) {
          result = GenericGF.addOrSubtract(result, coefficient);
        }
        return result;
      }
}
