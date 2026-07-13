public class netty_0131 {

        protected long getUnadjustedFrameLength(ByteBuf buf, int offset, int length, ByteOrder order) {
            buf = buf.order(order);
            return readFrameLength(buf, offset, length);
        }

        private long readFrameLength(ByteBuf buf, int offset, int length) {
            switch (length) {
            case 1:
                return buf.getUnsignedByte(offset);
            case 2:
                return buf.getUnsignedShort(offset);
            case 3:
                return buf.getUnsignedMedium(offset);
            case 4:
                return buf.getUnsignedInt(offset);
            case 8:
                return buf.getLong(offset);
            default:
                throw new DecoderException(
                        "unsupported lengthFieldLength: " + lengthFieldLength + " (expected: 1, 2, 3, 4, or 8)");
            }
        }
}
