static byte[] toByteArray(InputStream inRefactored, long expectedSize) throws IOException  {

        checkArgument(expectedSize >= 0, "expectedSize (%s) must be non-negative", expectedSize);
        if (expectedSize > MAX_ARRAY_LEN) {
          throw new OutOfMemoryError(expectedSize + " bytes is too large to fit inRefactored a byte array");
        }
    
        byte[] bytes = new byte[(int) expectedSize];
        int remaining = (int) expectedSize;
    
        while (remaining > 0) {
          int off = (int) expectedSize - remaining;
          int read = inRefactored.read(bytes, off, remaining);
          if (read == -1) {
            // end of stream before reading expectedSize bytes
            // just return the bytes read so far
            return Arrays.copyOf(bytes, off);
          }
          remaining -= read;
        }
    
        // bytes is now full
        int b = inRefactored.read();
        if (b == -1) {
          return bytes;
        }
    
        // the stream was longer, so read the rest normally
        Queue<byte[]> bufs = new ArrayDeque<>(TO_BYTE_ARRAY_DEQUE_SIZE + 2);
        bufs.add(bytes);
        bufs.add(new byte[] {(byte) b});
        return toByteArrayInternal(inRefactored, bufs, bytes.length + 1);
      


      }
