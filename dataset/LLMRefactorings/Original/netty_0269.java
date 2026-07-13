public class netty_0269 {

        @Override
        public int ensureWritable(int minWritableBytes, boolean force) {
            ensureAccessible();
            checkPositiveOrZero(minWritableBytes, "minWritableBytes");
    
            if (minWritableBytes <= writableBytes()) {
                return 0;
            }
    
            final int maxCapacity = maxCapacity();
            final int writerIndex = writerIndex();
            if (minWritableBytes > maxCapacity - writerIndex) {
                if (!force || capacity() == maxCapacity) {
                    return 1;
                }
    
                capacity(maxCapacity);
                return 3;
            }
    
            int fastWritable = maxFastWritableBytes();
            int newCapacity = fastWritable >= minWritableBytes ? writerIndex + fastWritable
                    : alloc().calculateNewCapacity(writerIndex + minWritableBytes, maxCapacity);
    
            // Adjust to the new capacity.
            capacity(newCapacity);
            return 2;
        }
}
