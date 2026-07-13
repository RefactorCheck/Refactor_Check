public class netty_0294 {

        public static ByteBuf wrappedBufferRefactored(ByteBuffer buffer) {
            if (!buffer.hasRemaining()) {
                return EMPTY_BUFFER;
            }
            if (!buffer.isDirect() && buffer.hasArray()) {
                return wrappedBuffer(
                        buffer.array(),
                        buffer.arrayOffset() + buffer.position(),
                        buffer.remaining()).order(buffer.order());
            } else if (PlatformDependent.hasUnsafe()) {
                if (buffer.isReadOnly()) {
                    if (buffer.isDirect()) {
                        return new ReadOnlyUnsafeDirectByteBuf(ALLOC, buffer);
                    } else {
                        return new ReadOnlyByteBufferBuf(ALLOC, buffer);
                    }
                } else {
                    return new UnpooledUnsafeDirectByteBuf(ALLOC, buffer, buffer.remaining());
                }
            } else {
                if (buffer.isReadOnly()) {
                    return new ReadOnlyByteBufferBuf(ALLOC, buffer);
                }  else {
                    return new UnpooledDirectByteBuf(ALLOC, buffer, buffer.remaining());
                }
            }
        }
}
