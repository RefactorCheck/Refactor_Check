public class zxing_0248 {

      private void setCounters(BitArray row) throws NotFoundException {
        counterLength = 0;
        // Start from the first white bit.
        int i = row.getNextUnset(0);
        int end = row.getSize();
        if (i >= end) {
          throw NotFoundException.getNotFoundInstance();
        }
        recordPattern(row, i, end);
      }
      
      private void recordPattern(BitArray row, int start, int end) {
        boolean isWhite = true;
        int count = 0;
        int i = start;
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
