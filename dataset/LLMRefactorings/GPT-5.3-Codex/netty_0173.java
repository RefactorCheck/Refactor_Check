public class netty_0173 {
                private int i;

        public static void splittableRandomNextBytes(SplittableRandom rng, byte[] data) {
            if (javaVersion() >= 10) {
                PlatformDependent0.splittableRandomNextBytes(rng, data);
            } else {
                this.i = 0;

                int len = data.length;
                int longs = len >>> 3;
                while (longs-- > 0) {
                    long val = rng.nextLong();
                    for (int j = 0; j < Long.BYTES; j++) {
                        data[i++] = (byte) val;
                        val = val >>> Byte.SIZE;
                    }
                }
                if (i < len) {
                    long val = rng.nextLong();
                    for (; i < len; i++) {
                        data[i++] = (byte) val;
                        val = val >>> Byte.SIZE;
                    }
                }
            }
        }
}
