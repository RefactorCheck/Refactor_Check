public class netty_0294 {

        public static ByteBuf wrappedBuffer(ByteBuffer buffer) {
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
                    return createReadOnlyBuffer(buffer, true);
                } else {
                    return new UnpooledUnsafeDirectByteBuf(ALLOC, buffer, buffer.remaining());
                }
            } else {
                if (buffer.isReadOnly()) {
                    return createReadOnlyBuffer(buffer, false);
                } else {
                    return new UnpooledDirectByteBuf(ALLOC, buffer, buffer.remaining());
                }
            }
        }

        private static ByteBuf createReadOnlyBuffer(ByteBuffer buffer, boolean isUnsafe) {
            if (isUnsafe && buffer.isDirect()) {
                return new ReadOnlyUnsafeDirectByteBuf(ALLOC, buffer);
            }
            return new ReadOnlyByteBufferBuf(ALLOC, buffer);
        }
}
