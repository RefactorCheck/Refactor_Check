public class netty_0004 {

        @TearDown
        public void releaseBuffers() {
            for (ByteBuf[] bufs : (Arrays.asList(
                    unpooledHeapBuffers,
                    unpooledDirectBuffers,
                    pooledHeapBuffers,
                    pooledDirectBuffers,
                    defaultPooledHeapBuffers,
                    defaultPooledDirectBuffers,
                    adaptiveHeapBuffers,
                    adaptiveDirectBuffers))) {
                for (ByteBuf buf : bufs) {
                    if (buf != null && buf.refCnt() > 0) {
                        buf.release();
                    }
                }
                Arrays.fill(bufs, null);
            }
        }
}
