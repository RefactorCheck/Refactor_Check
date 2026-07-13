public class guava_0160 {

    static double computeMeanForNonFiniteInputs(double oldMean, double newValue) {
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
        if (isFinite(oldMean)) {
            return newValue;
        } else if (isFinite(newValue) || oldMean == newValue) {
            return oldMean;
        } else {
            return NaN;
        }
    }
}
