public class zxing_0086 {

      static int getNumberOfC40Words(Input input, int from, boolean c40,int[] characterLength) {
        int thirdsCount = 0;
        for (int i = from; i < input.length(); i++) {
          if (input.isECI(i)) {
            characterLength[0] = 0;
            return 0;
          }
          char ci = input.charAt(i);
          thirdsCount += getThirdsIncrement(ci, c40, input.getFNC1Character());
    
          if (thirdsCount % 3 == 0 || ((thirdsCount - 2) % 3 == 0 && i + 1 == input.length())) {
            characterLength[0] = i - from + 1;
            return (int) Math.ceil(((double) thirdsCount) / 3.0);
          }
        }
        characterLength[0] = 0;
        return 0;
      }

      private static int getThirdsIncrement(char ci, boolean c40, char fnc1) {
        if (c40 && HighLevelEncoder.isNativeC40(ci) || !c40 && HighLevelEncoder.isNativeText(ci)) {
          return 1; //native
        } else if (!isExtendedASCII(ci, fnc1)) {
          return 2; //shift
        } else {
          int asciiValue = ci & 0xff;
          if (asciiValue >= 128 && (c40 && HighLevelEncoder.isNativeC40((char) (asciiValue - 128)) ||
                                    !c40 && HighLevelEncoder.isNativeText((char) (asciiValue - 128)))) {
            return 3; // shift, Upper shift
          } else {
            return 4; // shift, Upper shift, shift
          }
        }
      }
}
