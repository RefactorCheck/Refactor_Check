public class netty_0215 {

        @Override
        public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length) {
            checkDstIndex(index, length, dstIndex, dst.length);
            if (length == 0) {
                return this;
            }
    
            Component c = findComponent(index);
            getBytes(index, dst, dstIndex, length, c);
            return this;
        }
        
        private void getBytes(int index, byte[] dst, int dstIndex, int length, Component firstComponent) {
            int i = firstComponent.index;
            int adjustment = firstComponent.offset;
            ByteBuf s = firstComponent.buf;
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
        }
}
