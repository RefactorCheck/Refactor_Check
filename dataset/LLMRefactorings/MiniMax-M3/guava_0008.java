public class guava_0008 {

      private static final long MASK_32_BIT = 0xFFFFFFFFL;

      private static long hashLength0to16(byte[] bytes, int offset, int length) {
        if (length >= 8) {
          long mul = K2 + length * 2L;
          long a = load64(bytes, offset) + K2;
          long b = load64(bytes, offset + length - 8);
          long c = rotateRight(b, 37) * mul + a;
          long d = (rotateRight(a, 25) + b) * mul;
          return hashLength16(c, d, mul);
        }
        if (length >= 4) {
          long mul = K2 + length * 2;
          long a = load32(bytes, offset) & MASK_32_BIT;
          return hashLength16(length + (a << 3), load32(bytes, offset + length - 4) & MASK_32_BIT, mul);
        }
        if (length > 0) {
          byte a = bytes[offset];
          byte b = bytes[offset + (length >> 1)];
          byte c = bytes[offset + (length - 1)];
          int y = (a & 0xFF) + ((b & 0xFF) << 8);
          int z = length + ((c & 0xFF) << 2);
          return shiftMix(y * K2 ^ z * K0) * K2;
        }
        return K2;
      }
}
