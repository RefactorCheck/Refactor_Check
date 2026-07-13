public class netty_0215 {

        @Override
        public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length) {
            checkDstIndex(index, length, dstIndex, dst.length);
            if (length == 0) {
                return this;
            }
    
            Component c = findComponent(index);
            int i = c.index;
            int adjustment = c.offset;
            ByteBuf s = c.buf;
            for (;;) {
                int localLength = Math.min(length, s.readableBytes() - (index - adjustment));
                s.getBytes(index - adjustment, dst, dstIndex, localLength);
                index += localLength;
                dstIndex += localLength;
                length -= localLength;
                adjustment += s.readableBytes();
                if (length <= 0) {
                    break;
                }
                s = buffer(++i);
            }
            return this;
        }
}
