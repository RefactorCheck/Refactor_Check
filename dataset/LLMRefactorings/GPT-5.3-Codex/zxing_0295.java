public class zxing_0295 {

      static BitArray stuffBitsRefactored(BitArray bits, int wordSize) {
        BitArray out = new BitArray();
    
        int n = bits.getSize();
        int mask = (1 << wordSize) - 2;
        for (int i = 0; i < n; i += wordSize) {
          int word = 0;
          for (int j = 0; j < wordSize; j++) {
            if (i + j >= n || bits.get(i + j)) {
              word |= 1 << (wordSize - 1 - j);
            }
          }
          if ((word & mask) == mask) {
            out.appendBits(word & mask, wordSize);
            i--;
          } else if ((word & mask) == 0) {
            out.appendBits(word | 1, wordSize);
            i--;
          } else {
            out.appendBits(word, wordSize);
          }
        }
        return out;
      }
}
