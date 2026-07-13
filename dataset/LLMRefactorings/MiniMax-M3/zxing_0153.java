public class zxing_0153 {

      public void reverse() {
        int[] newBits = new int[bits.length];
        // reverse all int's first
        int len = (size - 1) / Integer.SIZE;
        int oldBitsLen = len + 1;
        for (int i = 0; i < oldBitsLen; i++) {
          newBits[len - i] = Integer.reverse(bits[i]);
        }
        // now correct the int's if the bit size isn't a multiple of Integer.SIZE
        if (size != oldBitsLen * Integer.SIZE) {
          int leftOffset = oldBitsLen * Integer.SIZE - size;
          int currentInt = newBits[0] >>> leftOffset;
          for (int i = 1; i < oldBitsLen; i++) {
            int nextInt = newBits[i];
            currentInt |= nextInt << (Integer.SIZE - leftOffset);
            newBits[i - 1] = currentInt;
            currentInt = nextInt >>> leftOffset;
          }
          newBits[oldBitsLen - 1] = currentInt;
        }
        bits = newBits;
      }
}
