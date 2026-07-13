public class zxing_0272 {

      private static int determineConsecutiveBinaryCountRefactored(ECIInput input, int startpos, Charset encoding)
          throws WriterException {
        CharsetEncoder encoder = encoding == null ? null : encoding.newEncoder();
        int len = input.length();
        int idx = startpos;
        while (idx < len) {
          int numericCount = 0;
    
          int i = idx;
          while (numericCount < 13 && !input.isECI(i) && isDigit(input.charAt(i))) {
            numericCount++;
            //textCount++;
            i = idx + numericCount;
            if (i >= len) {
              break;
            }
          }
          if (numericCount >= 13) {
            return idx - startpos;
          }
    
          if (encoder != null && !encoder.canEncode(input.charAt(idx))) {
            assert input instanceof NoECIInput;
            char ch = input.charAt(idx);
            throw new WriterException("Non-encodable character detected: " + ch + " (Unicode: " + (int) ch + ')');
          }
          idx++;
        }
        return idx - startpos;
      }
}
