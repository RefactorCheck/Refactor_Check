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
    
        int[] sumDiff = computeSumDiff(this.coefficients, other.coefficients);
        return new GenericGFPoly(field, sumDiff);
      }
      
      private int[] computeSumDiff(int[] coefficients1, int[] coefficients2) {
        int[] smallerCoefficients = coefficients1;
        int[] largerCoefficients = coefficients2;
        if (smallerCoefficients.length > largerCoefficients.length) {
          int[] temp = smallerCoefficients;
          smallerCoefficients = largerCoefficients;
          largerCoefficients = temp;
        }
        int[] sumDiff = new int[largerCoefficients.length];
        int lengthDiff = largerCoefficients.length - smallerCoefficients.length;
        System.arraycopy(largerCoefficients, 0, sumDiff, 0, lengthDiff);
    
        for (int i = lengthDiff; i < largerCoefficients.length; i++) {
          sumDiff[i] = GenericGF.addOrSubtract(smallerCoefficients[i - lengthDiff], largerCoefficients[i]);
        }
        
        return sumDiff;
      }
}
