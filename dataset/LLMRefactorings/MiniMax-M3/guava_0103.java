public class guava_0103 {

      public static void rotate(long[] array, int distance, int fromIndex, int toIndex) {
        checkNotNull(array);
        checkPositionIndexes(fromIndex, toIndex, array.length);
        if (array.length <= 1) {
          return;
        }
    
        int length = toIndex - fromIndex;
        int m = getRotationDistance(distance, length);
        int newFirstIndex = m + fromIndex;
        if (newFirstIndex == fromIndex) {
          return;
        }
    
        reverse(array, fromIndex, newFirstIndex);
        reverse(array, newFirstIndex, toIndex);
        reverse(array, fromIndex, toIndex);
      }

      private static int getRotationDistance(int distance, int length) {
        int m = -distance % length;
        return (m < 0) ? m + length : m;
      }
}
