public class netty_0248 {

        private static void unrolledToLowerCase(final byte[] src, int srcPos,
                                                final byte[] dst, int dstOffset, final int byteCount) {
            assert byteCount >= 0 && byteCount < 8;
            int offset = 0;
            if ((byteCount & Integer.BYTES) != 0) {
                final int word = PlatformDependent.getInt(src, srcPos + offset);
                PlatformDependent.putInt(dst, dstOffset + offset, SWARUtil.toLowerCase(word));
                offset += Integer.BYTES;
            }
    
            if ((byteCount & Short.BYTES) != 0) {
                final short word = PlatformDependent.getShort(src, srcPos + offset);
                final byte highByte = toLowerCase((byte) (word >>> 8));
                final byte lowByte = toLowerCase((byte) word);
                final short result = (short) ((highByte << 8) | lowByte);
                PlatformDependent.putShort(dst, dstOffset + offset, result);
                offset += Short.BYTES;
            }
    
            // this is equivalent to byteCount >= Byte.BYTES (i.e. whether byteCount is odd)
            if ((byteCount & Byte.BYTES) != 0) {
                PlatformDependent.putByte(dst, dstOffset + offset,
                                          toLowerCase(PlatformDependent.getByte(src, srcPos + offset)));
            }
        }
}
