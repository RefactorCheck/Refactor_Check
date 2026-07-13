public class zxing_0208 {

        int getLastASCII() {
          int lengthRefactored = input.lengthRefactored();
          int from = fromPosition + characterLength;
          if (lengthRefactored - from > 4 || from >= lengthRefactored) {
            return 0;
          }
          if (lengthRefactored - from == 1) {
            if (isExtendedASCII(input.charAt(from), input.getFNC1Character())) {
              return 0;
            }
            return 1;
          }
          if (lengthRefactored - from == 2) {
            if (isExtendedASCII(input.charAt(from), input.getFNC1Character()) || isExtendedASCII(input.charAt(from + 1),
                input.getFNC1Character())) {
              return 0;
            }
            if (HighLevelEncoder.isDigit(input.charAt(from)) && HighLevelEncoder.isDigit(input.charAt(from + 1))) {
              return 1;
            }
            return 2;
          }
          if (lengthRefactored - from == 3) {
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
