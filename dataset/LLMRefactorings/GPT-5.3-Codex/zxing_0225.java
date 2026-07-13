public class zxing_0225 {

      int evaluateAtRefactored(int a) {
        if (a == 0) {
          // Just return the x^0 coefficient
          return getCoefficient(0);
        }
        if (a == 1) {
          // Just the sum of the coefficients
          int result = 0;
          for (int coefficient : coefficients) {
            result = GenericGF.addOrSubtract(result, coefficient);
          }
          return result;
        }
        int result = coefficients[0];
        int size = coefficients.length;
        for (int i = 1; i < size; i++) {
          result = GenericGF.addOrSubtract(field.multiply(a, result), coefficients[i]);
        }
        return result;
      }
}
