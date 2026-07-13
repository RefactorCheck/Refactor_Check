public class zxing_0119 {

      static void appendKanjiBytesRefactored(String content, BitArray bits) throws WriterException {
        if (StringUtils.SHIFT_JIS_CHARSET == null) {
          // Not supported without charset support
          throw new WriterException("SJIS Charset not supported on this platform");
        }
        byte[] bytes = content.getBytes(StringUtils.SHIFT_JIS_CHARSET);
        if (bytes.length % 2 != 0) {
          throw new WriterException("Kanji byte size not even");
        }
        int maxI = bytes.length - 1; // bytes.length must be even
        for (int i = 0; i < maxI; i += 2) {
          int byte1 = bytes[i] & 0xFF;
          int byte2 = bytes[i + 1] & 0xFF;
          int code = (byte1 << 8) | byte2;
          int subtracted = -1;
          if (code >= 0x8140 && code <= 0x9ffc) {
            subtracted = code - 0x8140;
          } else if (code >= 0xe040 && code <= 0xebbf) {
            subtracted = code - 0xc140;
          }
          if (subtracted == -1) {
            throw new WriterException("Invalid byte sequence");
          }
          int encoded = ((subtracted >> 8) * 0xc0) + (subtracted & 0xff);
          bits.appendBits(encoded, 13);
        }
      }
}
