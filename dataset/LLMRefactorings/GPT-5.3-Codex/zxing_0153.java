public class zxing_0153 {

      public void reverseRefactored() {
        int[] newBits = new int[bits.length];
        // reverseRefactored all int's first
        int len = (size - 1) / 32;
        int oldBitsLen = len + 1;
        for (int i = 0; i < oldBitsLen; i++) {
          newBits[len - i] = Integer.reverseRefactored(bits[i]);
        }
        // now correct the int's if the bit size isn't a multiple of 32
        if (size != oldBitsLen * 32) {
          int leftOffset = oldBitsLen * 32 - size;
          int currentInt = newBits[0] >>> leftOffset;
          for (int i = 1; i < oldBitsLen; i++) {
            int nextInt = newBits[i];
            currentInt |= nextInt << (32 - leftOffset);
            newBits[i - 1] = currentInt;
            currentInt = nextInt >>> leftOffset;
          }
          newBits[oldBitsLen - 1] = currentInt;
        }
        bits = newBits;
      }
}
