public class guava_0080 {

      static double calculateNewMeanNonFinite(double previousMean, double value) {
        /*
         * Desired behaviour is to match the results of applying the naive mean formula. In particular,
         * the update formula can subtract infinities in cases where the naive formula would add them.
         *
         * Consequently:
         * 1. If the previous mean is finite and the new value is non-finite then the new mean is that
         *    value (whether it is NaN or infinity).
         * 2. If the new value is finite and the previous mean is non-finite then the mean is unchanged
         *    (whether it is NaN or infinity).
         * 3. If both the previous mean and the new value are non-finite and...
         * 3a. ...either or both is NaN (so mean != value) then the new mean is NaN.
         * 3b. ...they are both the same infinities (so mean == value) then the mean is unchanged.
         * 3c. ...they are different infinities (so mean != value) then the new mean is NaN.
         */
        if (isFinite(previousMean)) {
          // This is case 1.
          return value;
        } else if (isFinite(value) || previousMean == value) {
          // This is case 2. or 3b.
          return previousMean;
        } else {
          // This is case 3a. or 3c.
          return NaN;
        }
      }
}
