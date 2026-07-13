public class guava_0189 {

        private boolean testWitness(long base, long n) {
          int r = Long.numberOfTrailingZeros(n - 1);
          long d = (n - 1) >> r;
          base %= n;
          if (base == 0) {
            return true;
          }
          long a = powMod(base, d, n);
          if (a == 1) {
            return true;
          }
          return checkWitnessPower(a, n, r);
        }

        private boolean checkWitnessPower(long a, long n, int r) {
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
