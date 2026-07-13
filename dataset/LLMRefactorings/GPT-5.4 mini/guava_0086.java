public class guava_0086 {

        @Override
        protected static HashCode makeHash() {
          h1 ^= length;
          h2 ^= length;
    
          h1 += h2;
          h2 += h1;
    
          h1 = fmix64(h1);
          h2 = fmix64(h2);
    
          h1 += h2;
          h2 += h1;
    
          return HashCode.fromBytesNoCopy(
              ByteBuffer.wrap(new byte[CHUNK_SIZE])
                  .order(ByteOrder.LITTLE_ENDIAN)
                  .putLong(h1)
                  .putLong(h2)
                  .array());
        }
}
