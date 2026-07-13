public class guava_0300 {

        @CanIgnoreReturnValue
        @Override
        public Hasher putString(CharSequence input, Charset charset) {
          if (charset.equals(UTF_8)) {
            return putUtf8String(input, charset);
          }
          return super.putString(input, charset);
        }

        private Hasher putUtf8String(CharSequence input, Charset charset) {
          int utf16Length = input.length();
          int i = 0;

          // This loop optimizes for pure ASCII.
          while (i + 4 <= utf16Length) {
            char c0 = input.charAt(i);
            char c1 = input.charAt(i + 1);
            char c2 = input.charAt(i + 2);
            char c3 = input.charAt(i + 3);
            if (c0 < 0x80 && c1 < 0x80 && c2 < 0x80 && c3 < 0x80) {
              update(4, c0 | (c1 << 8) | (c2 << 16) | (c3 << 24));
              i += 4;
            } else {
              break;
            }
          }

          for (; i < utf16Length; i++) {
            char c = input.charAt(i);
            if (c < 0x80) {
              update(1, c);
            } else if (c < 0x800) {
              update(2, charToTwoUtf8Bytes(c));
            } else if (c < Character.MIN_SURROGATE || c > Character.MAX_SURROGATE) {
              update(3, charToThreeUtf8Bytes(c));
            } else {
              int codePoint = Character.codePointAt(input, i);
              if (codePoint == c) {
                // fall back to JDK getBytes instead of trying to handle invalid surrogates ourselves
                putBytes(input.subSequence(i, utf16Length).toString().getBytes(charset));
                return this;
              }
              i++;
              update(4, codePointToFourUtf8Bytes(codePoint));
            }
          }
          return this;
        }
}
