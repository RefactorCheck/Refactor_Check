public class zxing_0189 {

      private static int combins(int n, int r) {
        int maxDenom;
        int minDenom;
        if (n - r > r) {
          minDenom = r;
          maxDenom = n - r;
        } else {
          minDenom = n - r;
          maxDenom = r;
        }
        int val = 1;
        int j = 1;
        for (int i = n; i > maxDenom; i--) {
          val *= i;
          if (j <= minDenom) {
            val /= j;
            j++;
          }
        }
        while (j <= minDenom) {
          val /= j;
          j++;
        }
        return val;
      }
}
