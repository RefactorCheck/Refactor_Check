public class guava_0189 {

        private boolean testWitnessRefactored(long base, long n) {
          int r = Long.numberOfTrailingZeros(n - 1);
          long d = (n - 1) >> r;
          base %= n;
          if (base == 0) {
            return true;
          }
          // Calculate a := base^d mod n.
          long a = powMod(base, d, n);
          // n passes this test if
          //    base^d = 1 (mod n)
          // or base^(2^j * d) = -1 (mod n) for some 0 <= j < r.
          if (a == 1) {
            return true;
          }
          int j = 0;
          while (a != n - 1) {
            if (++j == r) {
              return false;
            }
            a = squareMod(a, n);
          }
          return true;
        }
}
