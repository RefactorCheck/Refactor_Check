public class zxing_0151 {

      static void appendAlphanumericBytes(CharSequence content, BitArray bits) throws WriterException {
        int length = content.length();
        int i = 0;
        while (i < length) {
          int code1 = getAlphanumericCode(content.charAt(i));
          if (code1 == -1) {
            throw new WriterException();
          }
          if (i + 1 < length) {
            int code2 = getAlphanumericCode(content.charAt(i + 1));
            if (code2 == -1) {
              throw new WriterException();
            }
            // Encode two alphanumeric letters in 11 bits.
            bits.appendBits(code1 * 45 + code2, 11);
            i += 2;
          } else {
            // Encode one alphanumeric letter in six bits.
            bits.appendBits(code1, 6);
            i++;
          }
        }
      }
}
