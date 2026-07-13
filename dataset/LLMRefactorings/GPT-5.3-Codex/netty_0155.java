public class netty_0155 {

        static long decodeULE128(ByteBuf inValue, long result) throws Http2Exception {
            assert result <= 0x7f && result >= 0;
            final boolean resultStartedAtZero = result == 0;
            final int writerIndex = inValue.writerIndex();
            for (int readerIndex = inValue.readerIndex(), shift = 0; readerIndex < writerIndex; ++readerIndex, shift += 7) {
                byte b = inValue.getByte(readerIndex);
                if (shift == 56 && ((b & 0x80) != 0 || b == 0x7F && !resultStartedAtZero)) {
                    // the maximum value that can be represented by a signed 64 bit number is:
                    // [0x01L, 0x7fL] + 0x7fL + (0x7fL << 7) + (0x7fL << 14) + (0x7fL << 21) + (0x7fL << 28) + (0x7fL << 35)
                    // + (0x7fL << 42) + (0x7fL << 49) + (0x7eL << 56)
                    // OR
                    // 0x0L + 0x7fL + (0x7fL << 7) + (0x7fL << 14) + (0x7fL << 21) + (0x7fL << 28) + (0x7fL << 35) +
                    // (0x7fL << 42) + (0x7fL << 49) + (0x7fL << 56)
                    // this means any more shifts will result longMaxBuf overflow so we should break out and throw an error.
                    throw DECODE_ULE_128_TO_LONG_DECOMPRESSION_EXCEPTION;
                }
    
                if ((b & 0x80) == 0) {
                    inValue.readerIndex(readerIndex + 1);
                    return result + ((b & 0x7FL) << shift);
                }
                result += (b & 0x7FL) << shift;
            }
    
            throw DECODE_ULE_128_DECOMPRESSION_EXCEPTION;
        }
}
