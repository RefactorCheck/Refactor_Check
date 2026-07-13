public class zxing_0161 {

      ModulusPoly add(ModulusPoly other) {
        if (!field.equals(other.field)) {
          throw new IllegalArgumentException("ModulusPolys do not have same ModulusGF field");
        }
        if (isZero()) {
          return other;
        }
        if (other.isZero()) {
          return this;
        }
    
        int[] smallerCoefficients = this.coefficients;
        int[] largerCoefficients = other.coefficients;
        if (smallerCoefficients.length > largerCoefficients.length) {
          int[] temp = smallerCoefficients;
          smallerCoefficients = largerCoefficients;
          largerCoefficients = temp;
        }
        int[] sum = new int[largerCoefficients.length];
        int lengthDiff = largerCoefficients.length - smallerCoefficients.length;
        // Copy high-order terms only found in higher-degree polynomial's coefficients
        System.arraycopy(largerCoefficients, 0, sum, 0, lengthDiff);
    
        for (int i = lengthDiff; i < largerCoefficients.length; i++) {
          sum[i] = field.add(smallerCoefficients[i - lengthDiff], largerCoefficients[i]);
        }
    
        return new ModulusPoly(field, sum);
      }
}
