public class guava_0086 {

        @Override
        protected HashCode makeHash() {
          h1 ^= length;
          h2 ^= length;
    
          mix();
    
          h1 = fmix64(h1);
          h2 = fmix64(h2);
    
          mix();
    
          return HashCode.fromBytesNoCopy(
              ByteBuffer.wrap(new byte[CHUNK_SIZE])
                  .order(ByteOrder.LITTLE_ENDIAN)
                  .putLong(h1)
                  .putLong(h2)
                  .array());
        }

        private void mix() {
          h1 += h2;
          h2 += h1;
        }
}
