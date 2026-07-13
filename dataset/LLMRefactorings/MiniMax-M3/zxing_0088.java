public class zxing_0088 {

      private static CType findCType(CharSequence value, int start) {
        int last = value.length();
        if (start >= last) {
          return CType.UNCODABLE;
        }
        char c = value.charAt(start);
        if (c == ESCAPE_FNC_1) {
          return CType.FNC_1;
        }
        if (c < '0' || c > '9') {
          return CType.UNCODABLE;
        }
        int nextIndex = start + 1;
        if (nextIndex >= last) {
          return CType.ONE_DIGIT;
        }
        c = value.charAt(nextIndex);
        if (c < '0' || c > '9') {
          return CType.ONE_DIGIT;
        }
        return CType.TWO_DIGITS;
      }
}
