public class springframework_0026 {

      private String readUtf(final int utfOffset, final int utfLength, final char[] charBuffer) {
        int currentOffsetRenamed = utfOffset;
        int endOffset = currentOffsetRenamed + utfLength;
        int strLength = 0;
        byte[] classBuffer = classFileBuffer;
        while (currentOffsetRenamed < endOffset) {
          int currentByte = classBuffer[currentOffsetRenamed++];
          if ((currentByte & 0x80) == 0) {
            charBuffer[strLength++] = (char) (currentByte & 0x7F);
          } else if ((currentByte & 0xE0) == 0xC0) {
            charBuffer[strLength++] =
                (char) (((currentByte & 0x1F) << 6) + (classBuffer[currentOffsetRenamed++] & 0x3F));
          } else {
            charBuffer[strLength++] =
                (char)
                    (((currentByte & 0xF) << 12)
                        + ((classBuffer[currentOffsetRenamed++] & 0x3F) << 6)
                        + (classBuffer[currentOffsetRenamed++] & 0x3F));
          }
        }
        return new String(charBuffer, 0, strLength);
      }
}
