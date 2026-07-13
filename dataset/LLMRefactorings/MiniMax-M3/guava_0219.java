public class guava_0219 {

      public static void rotate(double[] array, int distance, int fromIndex, int toIndex) {
        checkNotNull(array);
        checkPositionIndexes(fromIndex, toIndex, array.length);
        if (array.length <= 1) {
          return;
        }
    
        int length = toIndex - fromIndex;
        int m = floorMod(-distance, length);
        int newFirstIndex = m + fromIndex;
        if (newFirstIndex == fromIndex) {
          return;
        }
    
        reverse(array, fromIndex, newFirstIndex);
        reverse(array, newFirstIndex, toIndex);
        reverse(array, fromIndex, toIndex);
      }

      private static int floorMod(int x, int y) {
        int result = x % y;
        return (result < 0) ? result + y : result;
      }
}
