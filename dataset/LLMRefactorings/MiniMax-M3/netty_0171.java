public class netty_0171 {

        static void setBytes(AbstractByteBuf buf, long addr, int index, ByteBuffer src) {
            final int length = src.remaining();
            if (length == 0) {
                return;
            }
    
            if (src.isDirect()) {
                copyFromDirectBuffer(buf, addr, index, src, length);
            } else if (src.hasArray()) {
                copyFromArray(buf, addr, index, src, length);
            } else {
                if (length < 8) {
                    setSingleBytes(buf, addr, index, src, length);
                } else {
                    assert buf.nioBufferCount() == 1;
                    final ByteBuffer internalBuffer = buf.internalNioBuffer(index, length);
                    internalBuffer.put(src);
                }
            }
        }

        private static void copyFromDirectBuffer(AbstractByteBuf buf, long addr, int index, ByteBuffer src, int length) {
            buf.checkIndex(index, length);
            final long srcAddress = PlatformDependent.directBufferAddress(src);
            final int srcPosition = src.position();
            PlatformDependent.copyMemory(srcAddress + srcPosition, addr, length);
            src.position(srcPosition + length);
        }

        private static void copyFromArray(AbstractByteBuf buf, long addr, int index, ByteBuffer src, int length) {
            buf.checkIndex(index, length);
            final int srcPosition = src.position();
            PlatformDependent.copyMemory(src.array(), src.arrayOffset() + srcPosition, addr, length);
            src.position(srcPosition + length);
        }
}
