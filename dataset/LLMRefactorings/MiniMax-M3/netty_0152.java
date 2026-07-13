public class netty_0152 {

        @Override
        protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
            int length = msg.readableBytes() + lengthAdjustment;
            if (lengthIncludesLengthFieldLength) {
                length += lengthFieldLength;
            }
    
            checkPositiveOrZero(length, "length");
    
            writeLengthField(ctx, length, out);
            out.add(msg.retain());
        }

        private void writeLengthField(ChannelHandlerContext ctx, int length, List<Object> out) {
            switch (lengthFieldLength) {
            case 1:
                if (length >= 256) {
                    throw new IllegalArgumentException(
                            "length does not fit into a byte: " + length);
                }
                out.add(ctx.alloc().buffer(1).order(byteOrder).writeByte((byte) length));
                break;
            case 2:
                if (length >= 65536) {
                    throw new IllegalArgumentException(
                            "length does not fit into a short integer: " + length);
                }
                out.add(ctx.alloc().buffer(2).order(byteOrder).writeShort((short) length));
                break;
            case 3:
                if (length >= 16777216) {
                    throw new IllegalArgumentException(
                            "length does not fit into a medium integer: " + length);
                }
                out.add(ctx.alloc().buffer(3).order(byteOrder).writeMedium(length));
                break;
            case 4:
                out.add(ctx.alloc().buffer(4).order(byteOrder).writeInt(length));
                break;
            case 8:
                out.add(ctx.alloc().buffer(8).order(byteOrder).writeLong(length));
                break;
            default:
                throw new Error("Unexpected length field length: " + lengthFieldLength);
            }
        }
}
