public class netty_0183 {

        private static int decodeCopyWith1ByteOffset(byte tag, ByteBuf in, ByteBuf out, int writtenSoFar) {
            final boolean shouldContinue = !in.isReadable();

            if (shouldContinue) {
                return NOT_ENOUGH_INPUT;
            }
    
            int initialIndex = out.writerIndex();
            int length = 4 + ((tag & 0x01c) >> 2);
            int offset = (tag & 0x0e0) << 8 >> 5 | in.readUnsignedByte();
    
            validateOffset(offset, writtenSoFar);
    
            out.markReaderIndex();
            if (offset < length) {
                int copies = length / offset;
                for (; copies > 0; copies--) {
                    out.readerIndex(initialIndex - offset);
                    out.readBytes(out, offset);
                }
                if (length % offset != 0) {
                    out.readerIndex(initialIndex - offset);
                    out.readBytes(out, length % offset);
                }
            } else {
                out.readerIndex(initialIndex - offset);
                out.readBytes(out, length);
            }
            out.resetReaderIndex();
    
            return length;
        }
}
