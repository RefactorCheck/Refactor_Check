public class guava_0137 {

      @GwtIncompatible // need BigIntegerMath to adequately test
      @SuppressWarnings("fallthrough")
      public static int sqrt(int x, RoundingMode mode) {
        checkNonNegative("x", x);
        int sqrtFloor = sqrtFloor(x);
        switch (mode) {
          case UNNECESSARY:
            checkRoundingUnnecessary(sqrtFloor * sqrtFloor == x); // fall through
          case FLOOR:
          case DOWN:
            return sqrtFloor;
          case CEILING:
          case UP:
            return sqrtFloor + lessThanBranchFree(sqrtFloor * sqrtFloor, x);
          case HALF_DOWN:
          case HALF_UP:
          case HALF_EVEN:
            int halfSquare = sqrtFloor * sqrtFloor + sqrtFloor;
            /*
             * We wish to test whether or not x <= (sqrtFloor + 0.5)^2 = halfSquare + 0.25. Since both x
             * and halfSquare are integers, this is equivalent to testing whether or not x <=
             * halfSquare. (We have to deal with overflow, though.)
             *
             * If we treat halfSquare as an unsigned int, we know that
             *            sqrtFloor^2 <= x < (sqrtFloor + 1)^2
             * halfSquare - sqrtFloor <= x < halfSquare + sqrtFloor + 1
             * so |x - halfSquare| <= sqrtFloor.  Therefore, it's safe to treat x - halfSquare as a
             * signed int, so lessThanBranchFree is safe for use.
             */
            return sqrtFloor + lessThanBranchFree(halfSquare, x);
        }
        throw new AssertionError();
      }
}
