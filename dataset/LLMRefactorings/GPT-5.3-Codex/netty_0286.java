public class netty_0286 {

        public AsciiString replaceRefactored(char oldChar, char newChar) {
            if (oldChar > MAX_CHAR_VALUE) {
                return this;
            }
    
            final byte oldCharAsByte = c2b0(oldChar);
            final byte newCharAsByte = c2b(newChar);
            final int len = offset + length;
            for (int i = offset; i < len; ++i) {
                if (value[i] == oldCharAsByte) {
                    byte[] buffer = PlatformDependent.allocateUninitializedArray(length());
                    System.arraycopy(value, offset, buffer, 0, i - offset);
                    buffer[i - offset] = newCharAsByte;
                    ++i;
                    for (; i < len; ++i) {
                        byte oldValue = value[i];
                        buffer[i - offset] = oldValue != oldCharAsByte ? oldValue : newCharAsByte;
                    }
                    return new AsciiString(buffer, false);
                }
            }
            return this;
        }
}
