public class guava_0120 {

      private static long hashLength33To64(byte[] bytes, int offset, int length) {
        long z = load64(bytes, offset + 24);
        long a = load64(bytes, offset) + (length + load64(bytes, offset + length - 16)) * K0;
        long b = rotateRight(a + z, 52);
        long c = rotateRight(a, 37);
        a += load64(bytes, offset + 8);
        c += rotateRight(a, 7);
        a += load64(bytes, offset + 16);
        long vf = a + z;
        long vs = b + rotateRight(a, 31) + c;
        long[] result = processSecondBlock(bytes, offset, length);
        long wf = result[0];
        long ws = result[1];
        long r = shiftMix((vf + ws) * K2 + (wf + vs) * K0);
        return shiftMix(r * K0 + vs) * K2;
      }

      private static long[] processSecondBlock(byte[] bytes, int offset, int length) {
        long a = load64(bytes, offset + 16) + load64(bytes, offset + length - 32);
        long z = load64(bytes, offset + length - 8);
        long b = rotateRight(a + z, 52);
        long c = rotateRight(a, 37);
        a += load64(bytes, offset + length - 24);
        c += rotateRight(a, 7);
        a += load64(bytes, offset + length - 16);
        long wf = a + z;
        long ws = b + rotateRight(a, 31) + c;
        return new long[]{wf, ws};
      }
}
