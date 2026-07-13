public class guava_0080 {

      static double calculateNewMeanNonFinite(double previousMean, double value) {
        boolean previousIsFinite = isFinite(previousMean);
        boolean valueIsFinite = isFinite(value);
        if (previousIsFinite) {
          return value;
        } else if (valueIsFinite || previousMean == value) {
          return previousMean;
        } else {
          return NaN;
        }
      }
}
