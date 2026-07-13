public class netty_0286 {

    public AsciiString replace(char oldChar, char newChar) {
        if (oldChar > MAX_CHAR_VALUE) {
            return this;
        }

        final byte oldCharAsByte = c2b0(oldChar);
        final byte newCharAsByte = c2b(newChar);
        final int len = offset + length;
        for (int i = offset; i < len; ++i) {
            if (value[i] == oldCharAsByte) {
                return doReplace(i, oldCharAsByte, newCharAsByte, len);
            }
        }
        return this;
    }

    private AsciiString doReplace(int startIndex, byte oldCharAsByte, byte newCharAsByte, int len) {
        byte[] buffer = PlatformDependent.allocateUninitializedArray(length());
        System.arraycopy(value, offset, buffer, 0, startIndex - offset);
        buffer[startIndex - offset] = newCharAsByte;
        for (int i = startIndex + 1; i < len; ++i) {
            byte oldValue = value[i];
            buffer[i - offset] = oldValue != oldCharAsByte ? oldValue : newCharAsByte;
        }
        return new AsciiString(buffer, false);
    }
}
