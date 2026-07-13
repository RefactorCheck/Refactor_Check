public class kafka_0213 {

        private static int readUnsignedVarintNetty(ByteBuffer buffer) {
            byte nextByte = buffer.get();
            if (nextByte >= 0) {
                return nextByte;
            } else {
                int result = nextByte & 127;
                if ((nextByte = buffer.get()) >= 0) {
                    result |= nextByte << 7;
                } else {
                    result |= (nextByte & 127) << 7;
                    if ((nextByte = buffer.get()) >= 0) {
                        result |= nextByte << 14;
                    } else {
                        result |= (nextByte & 127) << 14;
                        if ((nextByte = buffer.get()) >= 0) {
                            result |= nextByte << 21;
                        } else {
                            result |= (nextByte & 127) << 21;
                            result |= (nextByte = buffer.get()) << 28;
                            if (nextByte < 0) {
                                throw new IllegalArgumentException();
                            }
                        }
                    }
                }
                return result;
            }
        }
}
