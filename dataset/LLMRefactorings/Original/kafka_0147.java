public class kafka_0147 {

        public static void writeUnsignedVarint(int value, ByteBuffer buffer) {
            if ((value & (0xFFFFFFFF << 7)) == 0) {
                buffer.put((byte) value);
            } else {
                buffer.put((byte) (value & 0x7F | 0x80));
                if ((value & (0xFFFFFFFF << 14)) == 0) {
                    buffer.put((byte) ((value >>> 7) & 0xFF));
                } else {
                    buffer.put((byte) ((value >>> 7) & 0x7F | 0x80));
                    if ((value & (0xFFFFFFFF << 21)) == 0) {
                        buffer.put((byte) ((value >>> 14) & 0xFF));
                    } else {
                        buffer.put((byte) ((value >>> 14) & 0x7F | 0x80));
                        if ((value & (0xFFFFFFFF << 28)) == 0) {
                            buffer.put((byte) ((value >>> 21) & 0xFF));
                        } else {
                            buffer.put((byte) ((value >>> 21) & 0x7F | 0x80));
                            buffer.put((byte) ((value >>> 28) & 0xFF));
                        }
                    }
                }
            }
        }
}
