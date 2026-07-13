public class guava_0083 {

      @GwtIncompatible // TODO
      @SuppressWarnings("fallthrough")
      public static long divide(long p, long q, RoundingMode mode) {
        checkNotNull(mode);
        long div = p / q; // throws if q == 0
        long rem = p - q * div; // equals p % q
    
        if (rem == 0) {
          return div;
        }
    
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
            increment = shouldIncrementForHalfMode(div, rem, q, mode);
            break;
          default:
            throw new AssertionError();
        }
        return increment ? div + signum : div;
      }
    
      private static boolean shouldIncrementForHalfMode(long div, long rem, long q, RoundingMode mode) {
        long absRem = abs(rem);
        long cmpRemToHalfDivisor = absRem - (abs(q) - absRem);
        if (cmpRemToHalfDivisor == 0) {
          return mode == RoundingMode.HALF_UP || (mode == RoundingMode.HALF_EVEN && (div & 1) != 0);
        }
        return cmpRemToHalfDivisor > 0;
      }
}
