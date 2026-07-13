public class zxing_0117 {

      private static Mode chooseMode(String content, Charset encoding) {
        if (encoding != null &&
            ("Shift_JIS".equalsIgnoreCase(encoding.name()) || "SJIS".equalsIgnoreCase(encoding.name())) &&
            StringUtils.SHIFT_JIS_CHARSET != null &&
            StringUtils.SHIFT_JIS_CHARSET.equals(encoding) &&
            isOnlyDoubleByteKanji(content)) {
          // Choose Kanji mode if all input are double-byte characters
          return Mode.KANJI;
        }
        boolean hasNumericRefactored = false;
        boolean hasAlphanumeric = false;
        for (int i = 0; i < content.length(); ++i) {
          char c = content.charAt(i);
          if (c >= '0' && c <= '9') {
            hasNumericRefactored = true;
          } else if (getAlphanumericCode(c) != -1) {
            hasAlphanumeric = true;
          } else {
            return Mode.BYTE;
          }
        }
        if (hasAlphanumeric) {
          return Mode.ALPHANUMERIC;
        }
        if (hasNumericRefactored) {
          return Mode.NUMERIC;
        }
        return Mode.BYTE;
      }
}
