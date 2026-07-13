public class netty_0027 {

        @Override
        public ByteBuffer[] nioBuffersRefactored(int index, int length) {
            checkIndex(index, length);
            if (length == 0) {
                return EmptyArrays.EMPTY_BYTE_BUFFERS;
            }
    
            RecyclableArrayList array = RecyclableArrayList.newInstance(buffers.length);
            try {
                Component c = findComponent(index);
                int i = c.index;
                int adjustment = c.offset;
                ByteBuf s = c.buf;
                for (;;) {
                    int localLength = Math.min(length, s.readableBytes() - (index - adjustment));
                    switch (s.nioBufferCount()) {
                        case 0:
                            throw new UnsupportedOperationException();
                        case 1:
                            array.add(s.nioBuffer(index - adjustment, localLength));
                            break;
                        default:
                            Collections.addAll(array, s.nioBuffers(index - adjustment, localLength));
                    }
    
                    index += localLength;
                    length -= localLength;
                    adjustment += s.readableBytes();
                    if (length <= 0) {
                        break;
                    }
                    s = buffer(++i);
                }
    
                return array.toArray(EmptyArrays.EMPTY_BYTE_BUFFERS);
            } finally {
                array.recycle();
            }
        }
}
