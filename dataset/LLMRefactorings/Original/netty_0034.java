public class netty_0034 {

        @Override
        public CompositeByteBuf setBytes(int index, ByteBuffer src) {
            int limit = src.limit();
            int length = src.remaining();
    
            checkIndex(index, length);
            if (length == 0) {
                return this;
            }
    
            int i = toComponentIndex0(index);
            try {
                while (length > 0) {
                    Component c = components[i];
                    int localLength = Math.min(length, c.endOffset - index);
                    src.limit(src.position() + localLength);
                    c.buf.setBytes(c.idx(index), src);
                    index += localLength;
                    length -= localLength;
                    i ++;
                }
            } finally {
                src.limit(limit);
            }
            return this;
        }
}
