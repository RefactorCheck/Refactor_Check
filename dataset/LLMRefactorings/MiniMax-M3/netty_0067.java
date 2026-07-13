public class netty_0067 {

        private static int unrolledLastIndexOf(final AbstractByteBuf buffer, final int fromIndex, final int byteCount,
                                               final byte value) {
            assert byteCount >= 0 && byteCount < 8;
            if (byteCount == 0) {
                return -1;
            }
            if (matches(buffer, fromIndex, value, 1)) {
                return fromIndex - 1;
            }
            if (byteCount == 1) {
                return -1;
            }
            if (matches(buffer, fromIndex, value, 2)) {
                return fromIndex - 2;
            }
            if (byteCount == 2) {
                return -1;
            }
            if (matches(buffer, fromIndex, value, 3)) {
                return fromIndex - 3;
            }
            if (byteCount == 3) {
                return -1;
            }
            if (matches(buffer, fromIndex, value, 4)) {
                return fromIndex - 4;
            }
            if (byteCount == 4) {
                return -1;
            }
            if (matches(buffer, fromIndex, value, 5)) {
                return fromIndex - 5;
            }
            if (byteCount == 5) {
                return -1;
            }
            if (matches(buffer, fromIndex, value, 6)) {
                return fromIndex - 6;
            }
            if (byteCount == 6) {
                return -1;
            }
            if (matches(buffer, fromIndex, value, 7)) {
                return fromIndex - 7;
            }
            return -1;
        }

        private static boolean matches(final AbstractByteBuf buffer, final int fromIndex, final byte value, final int offset) {
            return buffer._getByte(fromIndex - offset) == value;
        }
}
