public class zxing_0300 {

      GenericGFPoly addOrSubtract(GenericGFPoly other) {
        if (!field.equals(other.field)) {
          throw new IllegalArgumentException("GenericGFPolys do not have same GenericGF field");
        }
        if (isZero()) {
          return other;
        }
        if (other.isZero()) {
          return this;
        }
    
        int[] smallerCoefficientsRefactored = this.coefficients;
        int[] largerCoefficients = other.coefficients;
        if (smallerCoefficientsRefactored.length > largerCoefficients.length) {
          int[] temp = smallerCoefficientsRefactored;
          smallerCoefficientsRefactored = largerCoefficients;
          largerCoefficients = temp;
        }
        int[] sumDiff = new int[largerCoefficients.length];
        int lengthDiff = largerCoefficients.length - smallerCoefficientsRefactored.length;
        // Copy high-order terms only found in higher-degree polynomial's coefficients
        System.arraycopy(largerCoefficients, 0, sumDiff, 0, lengthDiff);
    
        for (int i = lengthDiff; i < largerCoefficients.length; i++) {
          sumDiff[i] = GenericGF.addOrSubtract(smallerCoefficientsRefactored[i - lengthDiff], largerCoefficients[i]);
        }
    
        return new GenericGFPoly(field, sumDiff);
      }
}
