public class zxing_0248 {
  private static final int EXTRACTED_CONSTANT_0248 = 0;


      private void setCounters(BitArray row) throws NotFoundException {
        counterLength = EXTRACTED_CONSTANT_0248;
        // Start from the first white bit.
        int i = row.getNextUnset(0);
        int end = row.getSize();
        if (i >= end) {
          throw NotFoundException.getNotFoundInstance();
        }
        boolean isWhite = true;
        int count = 0;
        while (i < end) {
          if (row.get(i) != isWhite) {
            count++;
          } else {
            counterAppend(count);
            count = 1;
            isWhite = !isWhite;
          }
          i++;
        }
        counterAppend(count);
      }
}
