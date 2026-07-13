public class kafka_0213 {

    private static final int VARINT_BYTE_MASK = 0x7F;
    private static final int VARINT_SHIFT_1 = 7;
    private static final int VARINT_SHIFT_2 = 14;
    private static final int VARINT_SHIFT_3 = 21;
    private static final int VARINT_SHIFT_4 = 28;

    private static int readUnsignedVarintNetty(ByteBuffer buffer) {
        byte tmp = buffer.get();
        if (tmp >= 0) {
            return tmp;
        } else {
            int result = tmp & VARINT_BYTE_MASK;
            if ((tmp = buffer.get()) >= 0) {
                result |= tmp << VARINT_SHIFT_1;
            } else {
                result |= (tmp & VARINT_BYTE_MASK) << VARINT_SHIFT_1;
                if ((tmp = buffer.get()) >= 0) {
                    result |= tmp << VARINT_SHIFT_2;
                } else {
                    result |= (tmp & VARINT_BYTE_MASK) << VARINT_SHIFT_2;
                    if ((tmp = buffer.get()) >= 0) {
                        result |= tmp << VARINT_SHIFT_3;
                    } else {
                        result |= (tmp & VARINT_BYTE_MASK) << VARINT_SHIFT_3;
                        result |= (tmp = buffer.get()) << VARINT_SHIFT_4;
                        if (tmp < 0) {
                            throw new IllegalArgumentException();
                        }
                    }
                }
            }
            return result;
        }
    }
}
