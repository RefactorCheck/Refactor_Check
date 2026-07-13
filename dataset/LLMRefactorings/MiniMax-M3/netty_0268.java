public class netty_0268 {

        static int decodeLiteral(byte tag, ByteBuf in, ByteBuf out) {
            in.markReaderIndex();
            int length;
            int type = tag >> 2 & 0x3F;
            switch(type) {
            case 60:
                if (!in.isReadable()) {
                    return NOT_ENOUGH_INPUT;
                }
                length = in.readUnsignedByte();
                break;
            case 61:
                if (in.readableBytes() < 2) {
                    return NOT_ENOUGH_INPUT;
                }
                length = in.readUnsignedShortLE();
                break;
            case 62:
                if (in.readableBytes() < 3) {
                    return NOT_ENOUGH_INPUT;
                }
                length = in.readUnsignedMediumLE();
                break;
            case 63:
                if (in.readableBytes() < 4) {
                    return NOT_ENOUGH_INPUT;
                }
                length = in.readIntLE();
                break;
            default:
                length = type;
            }
            length += 1;
    
            if (in.readableBytes() < length) {
                in.resetReaderIndex();
                return NOT_ENOUGH_INPUT;
            }
    
            out.writeBytes(in, length);
            return length;
        }
}
