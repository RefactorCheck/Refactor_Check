public class guava_0168 {

      @SuppressWarnings("ShortCircuitBoolean")
      public static long saturatedMultiply(long a, long b) {
        // see checkedMultiply for explanation
        int leadingZeros =
            Long.numberOfLeadingZeros(a)
                + Long.numberOfLeadingZeros(~a)
                + Long.numberOfLeadingZeros(b)
                + Long.numberOfLeadingZeros(~b);
        if (leadingZeros > Long.SIZE + 1) {
          return a * b;
        }
        // the return value if we will overflow (which we calculate by overflowing a long :) )
        long limit = Long.MAX_VALUE + ((a ^ b) >>> (Long.SIZE - 1));
        if (leadingZeros < Long.SIZE | (a < 0 & b == Long.MIN_VALUE)) {
          // overflow
          return limit;
        }
        long result = a * b;
        if (a == 0 || result / a == b) {
          return result;
        }
        return limit;
      }
}
