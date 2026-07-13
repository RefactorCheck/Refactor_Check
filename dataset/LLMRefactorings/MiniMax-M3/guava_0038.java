public class guava_0038 {

      public static void rotate(float[] array, int distance, int fromIndex, int toIndex) {
        // See Ints.rotate for more details about possible algorithms here.
        checkNotNull(array);
        checkPositionIndexes(fromIndex, toIndex, array.length);
        if (array.length <= 1) {
          return;
        }
    
        int length = toIndex - fromIndex;
        int newFirstIndex = getNewFirstIndex(distance, fromIndex, length);
        if (newFirstIndex == fromIndex) {
          return;
        }
    
        reverse(array, fromIndex, newFirstIndex);
        reverse(array, newFirstIndex, toIndex);
        reverse(array, fromIndex, toIndex);
      }
    
      private static int getNewFirstIndex(int distance, int fromIndex, int length) {
        int m = -distance % length;
        m = (m < 0) ? m + length : m;
        return m + fromIndex;
      }
}
