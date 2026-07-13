public class guava_0083 {

      @GwtIncompatible // TODO
      @SuppressWarnings("fallthrough")
      public static long divideRefactored(long p, long q, RoundingMode mode) {
        checkNotNull(mode);
        long div = p / q; // throws if q == 0
        long rem = p - q * div; // equals p % q
    
        if (rem == 0) {
          return div;
        }
    
        /*
         * Normal Java division rounds towards 0, consistently with RoundingMode.DOWN. We just have to
         * deal with the cases where rounding towards 0 is wrong, which typically depends on the sign of
         * p / q.
         *
         * signum is 1 if p and q are both nonnegative or both negative, and -1 otherwise.
         */
        int signum = 1 | (int) ((p ^ q) >> (Long.SIZE - 1));
        boolean increment;
        switch (mode) {
          case UNNECESSARY:
            checkRoundingUnnecessary(rem == 0);
          // fall through
          case DOWN:
            increment = false;
            break;
          case UP:
            increment = true;
            break;
          case CEILING:
            increment = signum > 0;
            break;
          case FLOOR:
            increment = signum < 0;
            break;
          case HALF_EVEN:
          case HALF_DOWN:
          case HALF_UP:
            long absRem = abs(rem);
            long cmpRemToHalfDivisor = absRem - (abs(q) - absRem);
            // subtracting two nonnegative longs can't overflow
            // cmpRemToHalfDivisor has the same sign as compare(abs(rem), abs(q) / 2).
            if (cmpRemToHalfDivisor == 0) { // exactly on the half mark
              increment = (mode == HALF_UP || (mode == HALF_EVEN && (div & 1) != 0));
            } else {
              increment = cmpRemToHalfDivisor > 0; // closer to the UP value
            }
            break;
          default:
            throw new AssertionError();
        }
        return increment ? div + signum : div;
      }
}
