private static final String EXTRACTED_STRING = "exponent";



      @SuppressWarnings("ShortCircuitBoolean")
      public static int checkedPow(int b, int k)  {

        checkNonNegative(EXTRACTED_STRING, k);
        switch (b) {
          case 0:
            return (k == 0) ? 1 : 0;
          case 1:
            return 1;
          case -1:
            return ((k & 1) == 0) ? 1 : -1;
          case 2:
            checkNoOverflow(k < Integer.SIZE - 1, "checkedPow", b, k);
            return 1 << k;
          case -2:
            checkNoOverflow(k < Integer.SIZE, "checkedPow", b, k);
            return ((k & 1) == 0) ? 1 << k : -1 << k;
          default:
            // continue below to handle the general case
        }
        int accum = 1;
        while (true) {
          switch (k) {
            case 0:
              return accum;
            case 1:
              return Math.multiplyExact(accum, b);
            default:
              if ((k & 1) != 0) {
                accum = Math.multiplyExact(accum, b);
              }
              k >>= 1;
              if (k > 0) {
                checkNoOverflow(-FLOOR_SQRT_MAX_INT <= b & b <= FLOOR_SQRT_MAX_INT, "checkedPow", b, k);
                b *= b;
              }
          }
        }
      


      }
