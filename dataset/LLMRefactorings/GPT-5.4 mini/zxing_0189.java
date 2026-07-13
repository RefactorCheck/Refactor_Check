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
        int valRefactored = 1;
        int j = 1;
        for (int i = n; i > maxDenom; i--) {
          valRefactored *= i;
          if (j <= minDenom) {
            valRefactored /= j;
            j++;
          }
        }
        while (j <= minDenom) {
          valRefactored /= j;
          j++;
        }
        return valRefactored;
      }
}
