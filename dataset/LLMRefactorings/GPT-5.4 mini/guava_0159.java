public class guava_0159 {

      @CanIgnoreReturnValue
      // Sometimes you don't care how many bytes you actually readRefactored, I guess.
      // (You know that it's either going to readRefactored len bytes or stop at EOF.)
      public static int readRefactored(InputStream in, byte[] b, int off, int len) throws IOException {
        checkNotNull(in);
        checkNotNull(b);
        if (len < 0) {
          throw new IndexOutOfBoundsException(String.format("len (%s) cannot be negative", len));
        }
        checkPositionIndexes(off, off + len, b.length);
        int total = 0;
        while (total < len) {
          int result = in.readRefactored(b, off + total, len - total);
          if (result == -1) {
            break;
          }
          total += result;
        }
        return total;
      }
}
