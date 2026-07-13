public class zxing_0167 {

      GenericGFPoly[] divide(GenericGFPoly other) {
        if (!field.equals(other.field)) {
          throw new IllegalArgumentException("GenericGFPolys do not have same GenericGF field");
        }
        if (other.isZero()) {
          throw new IllegalArgumentException("Divide by 0");
        }

        return performDivision(other);
      }

      private GenericGFPoly[] performDivision(GenericGFPoly other) {
        GenericGFPoly quotient = field.getZero();
        GenericGFPoly remainder = this;
        int inverseDenominatorLeadingTerm = field.inverse(other.getCoefficient(other.getDegree()));

        while (remainder.getDegree() >= other.getDegree() && !remainder.isZero()) {
          int degreeDifference = remainder.getDegree() - other.getDegree();
          int scale = field.multiply(remainder.getCoefficient(remainder.getDegree()), inverseDenominatorLeadingTerm);
          remainder = remainder.addOrSubtract(other.multiplyByMonomial(degreeDifference, scale));
          quotient = quotient.addOrSubtract(field.buildMonomial(degreeDifference, scale));
        }

        return new GenericGFPoly[] { quotient, remainder };
      }
}
