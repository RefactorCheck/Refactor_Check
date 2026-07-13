public class netty_0155 {

        static long decodeULE128(ByteBuf in, long result) throws Http2Exception {
            assert result <= 0x7f && result >= 0;
            final boolean resultStartedAtZero = result == 0;
            final int writerIndex = in.writerIndex();
            for (int readerIndex = in.readerIndex(), shift = 0; readerIndex < writerIndex; ++readerIndex, shift += 7) {
                byte b = in.getByte(readerIndex);
                if (isULE128Overflow(b, shift, resultStartedAtZero)) {
                    throw DECODE_ULE_128_TO_LONG_DECOMPRESSION_EXCEPTION;
                }
    
                if ((b & 0x80) == 0) {
                    in.readerIndex(readerIndex + 1);
                    return result + ((b & 0x7FL) << shift);
                }
                result += (b & 0x7FL) << shift;
            }
    
            throw DECODE_ULE_128_DECOMPRESSION_EXCEPTION;
        }
    
        private static boolean isULE128Overflow(byte b, int shift, boolean resultStartedAtZero) {
            return shift == 56 && ((b & 0x80) != 0 || b == 0x7F && !resultStartedAtZero);
        }
}
