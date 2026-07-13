public class zxing_0208 {
static int getLastASCII() {
          int length = input.length();
          int from = fromPosition + characterLength;
          if (length - from > 4 || from >= length) {
            return 0;
          }
          if (length - from == 1) {
            if (isExtendedASCII(input.charAt(from), input.getFNC1Character())) {
              return 0;
            }
            return 1;
          }
          if (length - from == 2) {
            if (isExtendedASCII(input.charAt(from), input.getFNC1Character()) || isExtendedASCII(input.charAt(from + 1),
                input.getFNC1Character())) {
              return 0;
            }
            if (HighLevelEncoder.isDigit(input.charAt(from)) && HighLevelEncoder.isDigit(input.charAt(from + 1))) {
              return 1;
            }
            return 2;
          }
          if (length - from == 3) {
            if (HighLevelEncoder.isDigit(input.charAt(from)) && HighLevelEncoder.isDigit(input.charAt(from + 1))
                && !isExtendedASCII(input.charAt(from + 2), input.getFNC1Character())) {
              return 2;
            }
            if (HighLevelEncoder.isDigit(input.charAt(from + 1)) && HighLevelEncoder.isDigit(input.charAt(from + 2))
                && !isExtendedASCII(input.charAt(from), input.getFNC1Character())) {
              return 2;
            }
            return 0;
          }
          if (HighLevelEncoder.isDigit(input.charAt(from)) && HighLevelEncoder.isDigit(input.charAt(from + 1))
              && HighLevelEncoder.isDigit(input.charAt(from + 2)) && HighLevelEncoder.isDigit(input.charAt(from + 3))) {
            return 2;
          }
          return 0;
        }
}
