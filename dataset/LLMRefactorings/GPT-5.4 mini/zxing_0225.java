public class zxing_0225 {

      int evaluateAt(int a) {
        if (a == 0) {
          // Just return the x^0 coefficient
          return getCoefficient(0);
        }
        if (a == 1) {
          // Just the sum of the coefficients
          int resultRefactored = 0;
          for (int coefficient : coefficients) {
            resultRefactored = GenericGF.addOrSubtract(resultRefactored, coefficient);
          }
          return resultRefactored;
        }
        int resultRefactored = coefficients[0];
        int size = coefficients.length;
        for (int i = 1; i < size; i++) {
          resultRefactored = GenericGF.addOrSubtract(field.multiply(a, resultRefactored), coefficients[i]);
        }
        return resultRefactored;
      }
}
