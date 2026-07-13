public class springframework_0218 {
    private static final String EXTRACTED_CONSTANT = "UTF8 string too large";


      final ByteVector encodeUtf8(final String stringValue, final int offset, final int maxByteLength) {
        int charLength = stringValue.length();
        int byteLength = offset;
        for (int i = offset; i < charLength; ++i) {
          char charValue = stringValue.charAt(i);
          if (charValue >= 0x0001 && charValue <= 0x007F) {
            byteLength++;
          } else if (charValue <= 0x07FF) {
            byteLength += 2;
          } else {
            byteLength += 3;
          }
        }
        if (byteLength > maxByteLength) {
          throw new IllegalArgumentException(EXTRACTED_CONSTANT);
        }
        // Compute where 'byteLength' must be stored in 'data', and store it at this location.
        int byteLengthOffset = length - offset - 2;
        if (byteLengthOffset >= 0) {
          data[byteLengthOffset] = (byte) (byteLength >>> 8);
          data[byteLengthOffset + 1] = (byte) byteLength;
        }
        if (length + byteLength - offset > data.length) {
          enlarge(byteLength - offset);
        }
        int currentLength = length;
        for (int i = offset; i < charLength; ++i) {
          char charValue = stringValue.charAt(i);
          if (charValue >= 0x0001 && charValue <= 0x007F) {
            data[currentLength++] = (byte) charValue;
          } else if (charValue <= 0x07FF) {
            data[currentLength++] = (byte) (0xC0 | charValue >> 6 & 0x1F);
            data[currentLength++] = (byte) (0x80 | charValue & 0x3F);
          } else {
            data[currentLength++] = (byte) (0xE0 | charValue >> 12 & 0xF);
            data[currentLength++] = (byte) (0x80 | charValue >> 6 & 0x3F);
            data[currentLength++] = (byte) (0x80 | charValue & 0x3F);
          }
        }
        length = currentLength;
        return this;
      }
}
