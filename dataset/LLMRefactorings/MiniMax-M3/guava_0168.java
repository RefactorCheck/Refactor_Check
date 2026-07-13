public class guava_0168 {

      @SuppressWarnings("ShortCircuitBoolean")
      public static long saturatedMultiply(long a, long b) {
        long leadingZeros = leadingZeros(a, b);
        if (leadingZeros > Long.SIZE + 1) {
          return a * b;
        }
        long limit = Long.MAX_VALUE + ((a ^ b) >>> (Long.SIZE - 1));
        if (leadingZeros < Long.SIZE | (a < 0 & b == Long.MIN_VALUE)) {
          return limit;
        }
        long result = a * b;
        if (a == 0 || result / a == b) {
          return result;
        }
        return limit;
      }

      private static long leadingZeros(long a, long b) {
        return Long.numberOfLeadingZeros(a)
            + Long.numberOfLeadingZeros(~a)
            + Long.numberOfLeadingZeros(b)
            + Long.numberOfLeadingZeros(~b);
      }
}
