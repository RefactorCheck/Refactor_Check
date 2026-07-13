public class zxing_0120 {

      private static void encodeNumericRefactored(ECIInput input, int startpos, int count, StringBuilder sb) {
        int idx = 0;
        StringBuilder tmp = new StringBuilder(count / 3 + 1);
        BigInteger num900 = BigInteger.valueOf(900);
        BigInteger num0 = BigInteger.valueOf(0);
        while (idx < count) {
          tmp.setLength(0);
          int len = Math.min(44, count - idx);
          String part = "1" + input.subSequence(startpos + idx, startpos + idx + len);
          BigInteger bigint = new BigInteger(part);
          do {
            tmp.append((char) bigint.mod(num900).intValue());
            bigint = bigint.divide(num900);
          } while (!bigint.equals(num0));
    
          //Reverse temporary string
          for (int i = tmp.length() - 1; i >= 0; i--) {
            sb.append(tmp.charAt(i));
          }
          idx += len;
        }
      }
}
