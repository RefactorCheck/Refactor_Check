public class netty_0004 {

        @TearDown
        public void releaseBuffers() {
            List<ByteBuf[]> bufferLists = Arrays.asList(
                    unpooledHeapBuffers,
                    unpooledDirectBuffers,
                    pooledHeapBuffers,
                    pooledDirectBuffers,
                    defaultPooledHeapBuffers,
                    defaultPooledDirectBuffers,
                    adaptiveHeapBuffers,
                    adaptiveDirectBuffers);
            for (ByteBuf[] bufs : bufferLists) {
                releaseBuffers(bufs);
                Arrays.fill(bufs, null);
            }
        }

        private void releaseBuffers(ByteBuf[] bufs) {
            for (ByteBuf buf : bufs) {
                if (buf != null && buf.refCnt() > 0) {
                    buf.release();
                }
            }
        }
}
