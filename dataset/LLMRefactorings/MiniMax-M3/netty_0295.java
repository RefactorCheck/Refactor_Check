public class netty_0295 {

        @Override
        public void addContent(ByteBuf buffer, boolean last)
                throws IOException {
            if (buffer != null) {
                long localsize = buffer.readableBytes();
                long newSize = size + localsize;
                try {
                    checkSize(newSize);
                } catch (IOException e) {
                    buffer.release();
                    throw e;
                }
                if (definedSize > 0 && definedSize < newSize) {
                    buffer.release();
                    throw new IOException("Out of size: " + newSize +
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
