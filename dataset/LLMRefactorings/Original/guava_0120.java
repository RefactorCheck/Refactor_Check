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
        a = load64(bytes, offset + 16) + load64(bytes, offset + length - 32);
        z = load64(bytes, offset + length - 8);
        b = rotateRight(a + z, 52);
        c = rotateRight(a, 37);
        a += load64(bytes, offset + length - 24);
        c += rotateRight(a, 7);
        a += load64(bytes, offset + length - 16);
        long wf = a + z;
        long ws = b + rotateRight(a, 31) + c;
        long r = shiftMix((vf + ws) * K2 + (wf + vs) * K0);
        return shiftMix(r * K0 + vs) * K2;
      }
}
