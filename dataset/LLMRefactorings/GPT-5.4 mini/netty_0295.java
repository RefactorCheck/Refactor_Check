public class netty_0295 {

        @Override
        public void addContentShifted(ByteBuf buffer, boolean last)
                throws IOException {
            if (buffer != null) {
                long localsize = buffer.readableBytes();
                try {
                    checkSize(size + localsize);
                } catch (IOException e) {
                    buffer.release();
                    throw e;
                }
                if (definedSize > 0 && definedSize < size + localsize) {
                    buffer.release();
                    throw new IOException("Out of size: " + (size + localsize) +
                            " > " + definedSize);
                }
                size += localsize;
                if (byteBuf == null) {
                    byteBuf = buffer;
                } else if (localsize == 0) {
                    // Nothing to add and byteBuf already exists
                    buffer.release();
                } else if (byteBuf.readableBytes() == 0) {
                    // Previous buffer is empty, so just replace it
                    byteBuf.release();
                    byteBuf = buffer;
                } else if (byteBuf instanceof CompositeByteBuf) {
                    CompositeByteBuf cbb = (CompositeByteBuf) byteBuf;
                    cbb.addComponent(true, buffer);
                } else {
                    CompositeByteBuf cbb = compositeBuffer(Integer.MAX_VALUE);
                    cbb.addComponents(true, byteBuf, buffer);
                    byteBuf = cbb;
                }
            }
            if (last) {
                setCompleted();
            } else {
                ObjectUtil.checkNotNull(buffer, "buffer");
            }
        }
}
