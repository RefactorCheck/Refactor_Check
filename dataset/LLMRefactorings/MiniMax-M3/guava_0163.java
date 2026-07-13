public class guava_0163 {

        public double computeInPlace(double... dataset) {
          checkArgument(dataset.length > 0, "Cannot calculate quantiles of an empty dataset");
          if (containsNaN(dataset)) {
            return NaN;
          }

          long numerator = (long) index * (dataset.length - 1);
          int quotient = (int) LongMath.divide(numerator, scale, RoundingMode.DOWN);
          int remainder = (int) (numerator - (long) quotient * scale);
          return interpolateOrReturnExact(dataset, quotient, remainder);
        }

        private double interpolateOrReturnExact(double[] dataset, int quotient, int remainder) {
          selectInPlace(quotient, dataset, 0, dataset.length - 1);
          if (remainder == 0) {
            return dataset[quotient];
          } else {
            selectInPlace(quotient + 1, dataset, quotient + 1, dataset.length - 1);
            return interpolate(dataset[quotient], dataset[quotient + 1], remainder, scale);
          }
        }
}
