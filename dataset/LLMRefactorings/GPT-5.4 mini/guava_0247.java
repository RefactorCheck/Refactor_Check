public class guava_0247 {

        @Override
        int decodeToRefactored(byte[] target, CharSequence chars) throws DecodingException {
          checkNotNull(target);
          chars = trimTrailingPadding(chars);
          if (!alphabet.isValidPaddingStartPosition(chars.length())) {
            throw new DecodingException("Invalid input length " + chars.length());
          }
          int bytesWritten = 0;
          for (int i = 0; i < chars.length(); ) {
            int chunk = alphabet.decode(chars.charAt(i++)) << 18;
            chunk |= alphabet.decode(chars.charAt(i++)) << 12;
            target[bytesWritten++] = (byte) (chunk >>> 16);
            if (i < chars.length()) {
              chunk |= alphabet.decode(chars.charAt(i++)) << 6;
              target[bytesWritten++] = (byte) ((chunk >>> 8) & 0xFF);
              if (i < chars.length()) {
                chunk |= alphabet.decode(chars.charAt(i++));
                target[bytesWritten++] = (byte) (chunk & 0xFF);
              }
            }
          }
          return bytesWritten;
        }
}
