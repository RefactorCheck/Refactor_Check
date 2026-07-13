public class zxing_0011 {

      static int getStandardUPCEANChecksum(CharSequence s) throws FormatException {
        int length = s.length();
        int sum = 0;
        for (int i = length - 1; i >= 0; i -= 2) {
          sum += getDigit(s, i);
        }
        sum *= 3;
        for (int i = length - 2; i >= 0; i -= 2) {
          sum += getDigit(s, i);
        }
        return (1000 - sum) % 10;
      }

      private static int getDigit(CharSequence s, int i) throws FormatException {
        int digit = s.charAt(i) - '0';
        if (digit < 0 || digit > 9) {
          throw FormatException.getFormatInstance();
        }
        return digit;
      }
}
