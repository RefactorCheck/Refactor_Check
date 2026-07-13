public class guava_0155 {

      public static void rotate(long[] array, int distance, int fromIndex, int toIndex) {
        checkNotNull(array);
        checkPositionIndexes(fromIndex, toIndex, array.length);
        if (array.length <= 1) {
          return;
        }
    
        int length = toIndex - fromIndex;
        int m = mod(-distance, length);
        int newFirstIndex = m + fromIndex;
        if (newFirstIndex == fromIndex) {
          return;
        }
    
        reverse(array, fromIndex, newFirstIndex);
        reverse(array, newFirstIndex, toIndex);
        reverse(array, fromIndex, toIndex);
      }
      
      private static int mod(int x, int length) {
        int result = x % length;
        return (result < 0) ? result + length : result;
      }
}
