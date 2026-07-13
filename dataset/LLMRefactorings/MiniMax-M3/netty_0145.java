public class netty_0145 {

        @Setup(Level.Iteration)
        public final void setup() throws Exception {
            ByteBufAllocator allocator = new PooledByteBufAllocator(true);
            initEngines(allocator);
            initHandshakeBuffers();
            initBuffers();
            if (!doHandshake()) {
                throw new IllegalStateException();
            }
            doSetup();
        }

        private void initBuffers() {
            cleanableWrapDstBuffer = allocateBuffer(clientEngine.getSession().getPacketBufferSize() << 2);
            cleanableWrapSrcBuffer = allocateBuffer(messageSize);
            wrapDstBuffer = cleanableWrapDstBuffer.buffer();
            wrapSrcBuffer = cleanableWrapDstBuffer.buffer();

            byte[] bytes = new byte[messageSize];
            ThreadLocalRandom.current().nextBytes(bytes);
            wrapSrcBuffer.put(bytes);
            wrapSrcBuffer.flip();
        }
}
