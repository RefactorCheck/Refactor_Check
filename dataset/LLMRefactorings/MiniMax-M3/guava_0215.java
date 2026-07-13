public class guava_0215 {

      public static void rotate(byte[] array, int distance, int fromIndex, int toIndex) {
        checkNotNull(array);
        checkPositionIndexes(fromIndex, toIndex, array.length);
        if (array.length <= 1) {
          return;
        }
    
        int newFirstIndex = computeNewFirstIndex(fromIndex, toIndex, distance);
        if (newFirstIndex == fromIndex) {
          return;
        }
    
        reverse(array, fromIndex, newFirstIndex);
        reverse(array, newFirstIndex, toIndex);
        reverse(array, fromIndex, toIndex);
      }
      
      private static int computeNewFirstIndex(int fromIndex, int toIndex, int distance) {
        int length = toIndex - fromIndex;
        int m = -distance % length;
        m = (m < 0) ? m + length : m;
        return m + fromIndex;
      }
}
