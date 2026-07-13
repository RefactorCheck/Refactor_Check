public class kafka_0044 {

        public static int hash32(byte[] data, int offset, int length, int seed) {
            int hash = seed;
            final int nblocks = length >> 2;
    
            // body
            for (int i = 0; i < nblocks; i++) {
                int i_4 = i << 2;
                int k = (data[offset + i_4] & 0xff)
                        | ((data[offset + i_4 + 1] & 0xff) << 8)
                        | ((data[offset + i_4 + 2] & 0xff) << 16)
                        | ((data[offset + i_4 + 3] & 0xff) << 24);
    
                hash = mix32(k, hash);
            }
    
            // tail
            int idx = nblocks << 2;
            int k1 = 0;
            final int tailLength = length - idx;
            switch (tailLength) {
                case 3:
                    k1 ^= data[offset + idx + 2] << 16;
                case 2:
                    k1 ^= data[offset + idx + 1] << 8;
                case 1:
                    k1 ^= data[offset + idx];
    
                    // mix functions
                    k1 *= C1_32;
                    k1 = Integer.rotateLeft(k1, R1_32);
                    k1 *= C2_32;
                    hash ^= k1;
            }
    
            return fmix32(length, hash);
        }
}
