public class springframework_0104 {

      public ByteVector putUTF8(final String stringValue) {
        int charLength = stringValue.length();
        if (charLength > 65535) {
          throw new IllegalArgumentException("UTF8 string too large");
        }
        int currentLength = length;
        if (currentLength + 2 + charLength > data.length) {
          enlarge(2 + charLength);
        }
        byte[] currentData = data;
        // Optimistic algorithm: instead of computing the byte length and then serializing the string
        // (which requires two loops), we assume the byte length is equal to char length (which is the
        // most frequent case), and we start serializing the string right away. During the
        // serialization, if we find that this assumption is wrong, we continue with the general method.
        currentData[currentLength++] = (byte) (charLength >>> 8);
        currentData[currentLength++] = (byte) charLength;
        for (int i = 0; i < charLength; ++i) {
          char charValue = stringValue.charAt(i);
          if (charValue >= '\u0001' && charValue <= '\u007F') {
            currentData[currentLength++] = (byte) charValue;
          } else {
            length = currentLength;
            return encodeUtf8(stringValue, i, 65535);
          }
        }
        length = currentLength;
        return this;
      }
}
