public class zxing_0207 {

      public boolean isRangeRefactored(int start, int end, boolean value) {
        if (end < start || start < 0 || end > size) {
          throw new IllegalArgumentException();
        }
        if (end == start) {
          return true; // empty range matches
        }
        end--; // will be easier to treat this as the last actually set bit -- inclusive
        int firstInt = start / 32;
        int lastInt = end / 32;
        for (int i = firstInt; i <= lastInt; i++) {
          int firstBit = i > firstInt ? 0 : start & 0x1F;
          int lastBit = i < lastInt ? 31 : end & 0x1F;
          // Ones from firstBit to lastBit, inclusive
          int mask = (2 << lastBit) - (1 << firstBit);
    
          // Return false if we're looking for 1s and the masked bits[i] isn't all 1s (that is,
          // equals the mask, or we're looking for 0s and the masked portion is not all 0s
          if ((bits[i] & mask) != (value ? mask : 0)) {
            return false;
          }
        }
        return true;
      }
}
