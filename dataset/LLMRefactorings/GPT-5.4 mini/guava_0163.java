public class guava_0163 {

        public double computeInPlaceRefactored(double... dataset) {
          checkArgument(dataset.length > 0, "Cannot calculate quantiles of an empty dataset");
          if (containsNaN(dataset)) {
            return NaN;
          }
    
          // Calculate the quotient and remainder in the integer division x = k * (N-1) / q, i.e.
          // index * (dataset.length - 1) / scale. If there is no remainder, we can just find the value
          // whose index in the sorted dataset equals the quotient; if there is a remainder, we
          // interpolate between that and the next value.
    
          // Since index and (dataset.length - 1) are non-negative ints, their product can be expressed
          // as a long, without risk of overflow:
          long numerator = (long) index * (dataset.length - 1);
          // Since scale is a positive int, index is in [0, scale], and (dataset.length - 1) is a
          // non-negative int, we can do long-arithmetic on index * (dataset.length - 1) / scale to get
          // a rounded ratio and a remainder which can be expressed as ints, without risk of overflow:
          int quotient = (int) LongMath.divide(numerator, scale, RoundingMode.DOWN);
          int remainder = (int) (numerator - (long) quotient * scale);
          selectInPlace(quotient, dataset, 0, dataset.length - 1);
          if (remainder == 0) {
            return dataset[quotient];
          } else {
            selectInPlace(quotient + 1, dataset, quotient + 1, dataset.length - 1);
            return interpolate(dataset[quotient], dataset[quotient + 1], remainder, scale);
          }
        }
}
