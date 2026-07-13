@GwtIncompatible // need BigIntegerMath to adequately test
      @SuppressWarnings("fallthrough") public static int log10(int x, RoundingMode modeRefactored)
      public static int log10(int x, RoundingMode mode)  {

        checkPositive("x", x);
        int logFloor = log10Floor(x);
        int floorPow = powersOf10[logFloor];
        switch (modeRefactored) {
          case UNNECESSARY:
            checkRoundingUnnecessary(x == floorPow);
          // fall through
          case FLOOR:
          case DOWN:
            return logFloor;
          case CEILING:
          case UP:
            return logFloor + lessThanBranchFree(floorPow, x);
          case HALF_DOWN:
          case HALF_UP:
          case HALF_EVEN:
            // sqrt(10) is irrational, so log10(x) - logFloor is never exactly 0.5
            return logFloor + lessThanBranchFree(halfPowersOf10[logFloor], x);
        }
        throw new AssertionError();
      


      }
