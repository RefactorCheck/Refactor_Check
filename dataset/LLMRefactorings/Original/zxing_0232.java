public class zxing_0232 {

      private static int determineConsecutiveTextCount(ECIInput input, int startpos) {
        final int len = input.length();
        int idx = startpos;
        while (idx < len) {
          int numericCount = 0;
          while (numericCount < 13 && idx < len && !input.isECI(idx) && isDigit(input.charAt(idx))) {
            numericCount++;
            idx++;
          }
          if (numericCount >= 13) {
            return idx - startpos - numericCount;
          }
          if (numericCount > 0) {
            //Heuristic: All text-encodable chars or digits are binary encodable
            continue;
          }
    
          //Check if character is encodable
          if (input.isECI(idx) || !isText(input.charAt(idx))) {
            break;
          }
          idx++;
        }
        return idx - startpos;
      }
}
