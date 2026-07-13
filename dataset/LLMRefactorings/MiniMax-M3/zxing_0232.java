public class zxing_0232 {
      private static final int MAX_NUMERIC_COUNT = 13;
      
      private static int determineConsecutiveTextCount(ECIInput input, int startpos) {
        final int len = input.length();
        int idx = startpos;
        while (idx < len) {
          int numericCount = 0;
          while (numericCount < MAX_NUMERIC_COUNT && idx < len && !input.isECI(idx) && isDigit(input.charAt(idx))) {
            numericCount++;
            idx++;
          }
          if (numericCount >= MAX_NUMERIC_COUNT) {
            return idx - startpos - numericCount;
          }
          if (numericCount > 0) {
            continue;
          }
    
          if (input.isECI(idx) || !isText(input.charAt(idx))) {
            break;
          }
          idx++;
        }
        return idx - startpos;
      }
}
