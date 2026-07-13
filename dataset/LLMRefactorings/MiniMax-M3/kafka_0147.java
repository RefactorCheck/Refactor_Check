public class kafka_0147 {

    private static final int VARINT_SHIFT_7 = 7;
    private static final int VARINT_SHIFT_14 = 14;
    private static final int VARINT_SHIFT_21 = 21;
    private static final int VARINT_SHIFT_28 = 28;
    private static final int VARINT_BYTE_MASK = 0xFF;
    private static final int VARINT_SEVEN_BIT_MASK = 0x7F;
    private static final int VARINT_HIGH_BIT = 0x80;
    private static final int VARINT_HIGH_BIT_MASK = 0xFFFFFFFF;

    public static void writeUnsignedVarint(int value, ByteBuffer buffer) {
        if ((value & (VARINT_HIGH_BIT_MASK << VARINT_SHIFT_7)) == 0) {
            buffer.put((byte) value);
        } else {
            buffer.put((byte) (value & VARINT_SEVEN_BIT_MASK | VARINT_HIGH_BIT));
            if ((value & (VARINT_HIGH_BIT_MASK << VARINT_SHIFT_14)) == 0) {
                buffer.put((byte) ((value >>> VARINT_SHIFT_7) & VARINT_BYTE_MASK));
            } else {
                buffer.put((byte) ((value >>> VARINT_SHIFT_7) & VARINT_SEVEN_BIT_MASK | VARINT_HIGH_BIT));
                if ((value & (VARINT_HIGH_BIT_MASK << VARINT_SHIFT_21)) == 0) {
                    buffer.put((byte) ((value >>> VARINT_SHIFT_14) & VARINT_BYTE_MASK));
                } else {
                    buffer.put((byte) ((value >>> VARINT_SHIFT_14) & VARINT_SEVEN_BIT_MASK | VARINT_HIGH_BIT));
                    if ((value & (VARINT_HIGH_BIT_MASK << VARINT_SHIFT_28)) == 0) {
                        buffer.put((byte) ((value >>> VARINT_SHIFT_21) & VARINT_BYTE_MASK));
                    } else {
                        buffer.put((byte) ((value >>> VARINT_SHIFT_21) & VARINT_SEVEN_BIT_MASK | VARINT_HIGH_BIT));
                        buffer.put((byte) ((value >>> VARINT_SHIFT_28) & VARINT_BYTE_MASK));
                    }
                }
            }
        }
    }
}
