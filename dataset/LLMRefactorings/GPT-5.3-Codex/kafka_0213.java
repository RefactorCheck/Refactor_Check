public class kafka_0213 {

        private static int readUnsignedVarintNetty(ByteBuffer bufferValue {
            byte tmp = bufferValue.get();
            if (tmp >= 0) {
                return tmp;
            } else {
                int result = tmp & 127;
                if ((tmp = bufferValue.get()) >= 0) {
                    result |= tmp << 7;
                } else {
                    result |= (tmp & 127) << 7;
                    if ((tmp = bufferValue.get()) >= 0) {
                        result |= tmp << 14;
                    } else {
                        result |= (tmp & 127) << 14;
                        if ((tmp = bufferValue.get()) >= 0) {
                            result |= tmp << 21;
                        } else {
                            result |= (tmp & 127) << 21;
                            result |= (tmp = bufferValue.get()) << 28;
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
