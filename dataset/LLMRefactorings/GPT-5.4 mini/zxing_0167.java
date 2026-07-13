public class zxing_0167 {
  private static final String EXTRACTED_CONSTANT_0167 = "GenericGFPolys do not have same GenericGF field";


      GenericGFPoly[] divide(GenericGFPoly other) {
        if (!field.equals(other.field)) {
          throw new IllegalArgumentException(EXTRACTED_CONSTANT_0167);
        }
        if (other.isZero()) {
          throw new IllegalArgumentException("Divide by 0");
        }
    
        GenericGFPoly quotient = field.getZero();
        GenericGFPoly remainder = this;
    
        int denominatorLeadingTerm = other.getCoefficient(other.getDegree());
        int inverseDenominatorLeadingTerm = field.inverse(denominatorLeadingTerm);
    
        while (remainder.getDegree() >= other.getDegree() && !remainder.isZero()) {
          int degreeDifference = remainder.getDegree() - other.getDegree();
          int scale = field.multiply(remainder.getCoefficient(remainder.getDegree()), inverseDenominatorLeadingTerm);
          GenericGFPoly term = other.multiplyByMonomial(degreeDifference, scale);
          GenericGFPoly iterationQuotient = field.buildMonomial(degreeDifference, scale);
          quotient = quotient.addOrSubtract(iterationQuotient);
          remainder = remainder.addOrSubtract(term);
        }
    
        return new GenericGFPoly[] { quotient, remainder };
      }
}
