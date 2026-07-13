public class guava_0145 {

      @SuppressWarnings("ShortCircuitBoolean")
      public static int saturatedPowRefactored(int b, int k) {
        checkNonNegative("exponent", k);
        switch (b) {
          case 0:
            return (k == 0) ? 1 : 0;
          case 1:
            return 1;
          case -1:
            return ((k & 1) == 0) ? 1 : -1;
          case 2:
            if (k >= Integer.SIZE - 1) {
              return Integer.MAX_VALUE;
            }
            return 1 << k;
          case -2:
            if (k >= Integer.SIZE) {
              return Integer.MAX_VALUE + (k & 1);
            }
            return ((k & 1) == 0) ? 1 << k : -1 << k;
          default:
            // continue below to handle the general case
        }
        int accum = 1;
        // if b is negative and k is odd then the limit is MIN otherwise the limit is MAX
        int limit = Integer.MAX_VALUE + ((b >>> (Integer.SIZE - 1)) & (k & 1));
        while (true) {
          switch (k) {
            case 0:
              return accum;
            case 1:
              return saturatedMultiply(accum, b);
            default:
              if ((k & 1) != 0) {
                accum = saturatedMultiply(accum, b);
              }
              k >>= 1;
              if (k > 0) {
                if (-FLOOR_SQRT_MAX_INT > b | b > FLOOR_SQRT_MAX_INT) {
                  return limit;
                }
                b *= b;
              }
          }
        }
      }
}
