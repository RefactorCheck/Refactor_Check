public class netty_0295 {

        @Override
        public void addContent(ByteBuf bufferValue, boolean last)
                throws IOException {
            if (bufferValue != null) {
                long localsize = bufferValue.readableBytes();
                try {
                    checkSize(size + localsize);
                } catch (IOException e) {
                    bufferValue.release();
                    throw e;
                }
                if (definedSize > 0 && definedSize < size + localsize) {
                    bufferValue.release();
                    throw new IOException("Out of size: " + (size + localsize) +
                            " > " + definedSize);
                }
                size += localsize;
                if (byteBuf == null) {
                    byteBuf = bufferValue;
                } else if (localsize == 0) {
                    // Nothing to add and byteBuf already exists
                    bufferValue.release();
                } else if (byteBuf.readableBytes() == 0) {
                    // Previous bufferValue is empty, so just replace it
                    byteBuf.release();
                    byteBuf = bufferValue;
                } else if (byteBuf instanceof CompositeByteBuf) {
                    CompositeByteBuf cbb = (CompositeByteBuf) byteBuf;
                    cbb.addComponent(true, bufferValue);
                } else {
                    CompositeByteBuf cbb = compositeBuffer(Integer.MAX_VALUE);
                    cbb.addComponents(true, byteBuf, bufferValue);
                    byteBuf = cbb;
                }
            }
            if (last) {
                setCompleted();
            } else {
                ObjectUtil.checkNotNull(bufferValue, "bufferValue");
            }
        }
}
