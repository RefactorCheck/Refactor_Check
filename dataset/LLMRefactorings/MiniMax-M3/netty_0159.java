public class netty_0159 {

    public static byte[] getBytes(ByteBuf buf, int start, int length, boolean copy) {
        int capacity = buf.capacity();
        if (isOutOfBounds(start, length, capacity)) {
            throw new IndexOutOfBoundsException(createErrorMessage(start, length, capacity));
        }

        if (buf.hasArray()) {
            int baseOffset = buf.arrayOffset() + start;
            byte[] bytes = buf.array();
            if (copy || baseOffset != 0 || length != bytes.length) {
                return Arrays.copyOfRange(bytes, baseOffset, baseOffset + length);
            } else {
                return bytes;
            }
        }

        byte[] bytes = PlatformDependent.allocateUninitializedArray(length);
        buf.getBytes(start, bytes);
        return bytes;
    }

    private static String createErrorMessage(int start, int length, int capacity) {
        return "expected: " + "0 <= start(" + start + ") <= start + length(" + length
                + ") <= " + "buf.capacity(" + capacity + ')';
    }
}
