public class zxing_0178 {

      private static final int MASK_8_BITS = 255;

      private static String encodeToCodewords(CharSequence sb) {
        int len = sb.length();
        if (len == 0) {
          throw new IllegalStateException("StringBuilder must not be empty");
        }
        char c1 = sb.charAt(0);
        char c2 = len >= 2 ? sb.charAt(1) : 0;
        char c3 = len >= 3 ? sb.charAt(2) : 0;
        char c4 = len >= 4 ? sb.charAt(3) : 0;

        int v = (c1 << 18) + (c2 << 12) + (c3 << 6) + c4;
        char cw1 = (char) ((v >> 16) & MASK_8_BITS);
        char cw2 = (char) ((v >> 8) & MASK_8_BITS);
        char cw3 = (char) (v & MASK_8_BITS);
        StringBuilder res = new StringBuilder(3);
        res.append(cw1);
        if (len >= 2) {
          res.append(cw2);
        }
        if (len >= 3) {
          res.append(cw3);
        }
        return res.toString();
      }
}
