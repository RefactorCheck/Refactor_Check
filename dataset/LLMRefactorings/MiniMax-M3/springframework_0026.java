public class springframework_0026 {

      private String readUtf(final int utfOffset, final int utfLength, final char[] charBuffer) {
        int currentOffset = utfOffset;
        int endOffset = currentOffset + utfLength;
        int strLength = 0;
        byte[] classBuffer = classFileBuffer;
        while (currentOffset < endOffset) {
          currentOffset = readUtfChar(classBuffer, currentOffset, charBuffer, strLength);
          strLength++;
        }
        return new String(charBuffer, 0, strLength);
      }

      private int readUtfChar(byte[] classBuffer, int currentOffset, char[] charBuffer, int strLength) {
        int currentByte = classBuffer[currentOffset++];
        if ((currentByte & 0x80) == 0) {
          charBuffer[strLength] = (char) (currentByte & 0x7F);
          return currentOffset;
        } else if ((currentByte & 0xE0) == 0xC0) {
          charBuffer[strLength] =
              (char) (((currentByte & 0x1F) << 6) + (classBuffer[currentOffset++] & 0x3F));
          return currentOffset;
        } else {
          charBuffer[strLength] =
              (char)
                  (((currentByte & 0xF) << 12)
                      + ((classBuffer[currentOffset++] & 0x3F) << 6)
                      + (classBuffer[currentOffset++] & 0x3F));
          return currentOffset;
        }
      }
}
