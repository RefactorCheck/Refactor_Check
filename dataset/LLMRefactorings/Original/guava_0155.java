public class guava_0155 {

      public static void rotate(long[] array, int distance, int fromIndex, int toIndex) {
        // See Ints.rotate for more details about possible algorithms here.
        checkNotNull(array);
        checkPositionIndexes(fromIndex, toIndex, array.length);
        if (array.length <= 1) {
          return;
        }
    
        int length = toIndex - fromIndex;
        // Obtain m = (-distance mod length), a non-negative value less than "length". This is how many
        // places left to rotate.
        int m = -distance % length;
        m = (m < 0) ? m + length : m;
        // The current index of what will become the first element of the rotated section.
        int newFirstIndex = m + fromIndex;
        if (newFirstIndex == fromIndex) {
          return;
        }
    
        reverse(array, fromIndex, newFirstIndex);
        reverse(array, newFirstIndex, toIndex);
        reverse(array, fromIndex, toIndex);
      }
}
