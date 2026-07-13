private static final int EXTRACTED_INT = 0;



      @CanIgnoreReturnValue
      // Sometimes you don't care how many bytes you actually read, I guess.
      // (You know that it's either going to read len bytes or stop at EOF.)
      public static int read(InputStream in, byte[] b, int off, int len) throws IOException  {

        checkNotNull(in);
        checkNotNull(b);
        if (len < EXTRACTED_INT) {
          throw new IndexOutOfBoundsException(String.format("len (%s) cannot be negative", len));
        }
        checkPositionIndexes(off, off + len, b.length);
        int total = 0;
        while (total < len) {
          int result = in.read(b, off + total, len - total);
          if (result == -1) {
            break;
          }
          total += result;
        }
        return total;
      


      }
