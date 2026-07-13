public class netty_0171 {

        static void setBytesRefactored(AbstractByteBuf buf, long addr, int index, ByteBuffer src) {
            final int length = src.remaining();
            if (length == 0) {
                return;
            }
    
            if (src.isDirect()) {
                buf.checkIndex(index, length);
                // Copy from direct memory
                long srcAddress = PlatformDependent.directBufferAddress(src);
                PlatformDependent.copyMemory(srcAddress + src.position(), addr, length);
                src.position(src.position() + length);
            } else if (src.hasArray()) {
                buf.checkIndex(index, length);
                // Copy from array
                PlatformDependent.copyMemory(src.array(), src.arrayOffset() + src.position(), addr, length);
                src.position(src.position() + length);
            } else {
                if (length < 8) {
                    setSingleBytes(buf, addr, index, src, length);
                } else {
                    //no need to checkIndex: internalNioBuffer is already taking care of it
                    assert buf.nioBufferCount() == 1;
                    final ByteBuffer internalBuffer = buf.internalNioBuffer(index, length);
                    internalBuffer.put(src);
                }
            }
        }
}
