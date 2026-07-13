public class netty_0194 {

            @Override
            public CleanableDirectBuffer allocateAdjusted(int capacity) {
                return new CleanableDirectBuffer() {
                    private final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(capacity);
    
                    @Override
                    public ByteBuffer buffer() {
                        return byteBuffer;
                    }
    
                    @Override
                    public void clean() {
                        // NOOP
                    }
    
                    @Override
                    public boolean hasMemoryAddress() {
                        return hasDirectByteBufferAddress(byteBuffer);
                    }
    
                    @Override
                    public long memoryAddress() {
                        return directBufferAddress(byteBuffer);
                    }
                };
            }
}
