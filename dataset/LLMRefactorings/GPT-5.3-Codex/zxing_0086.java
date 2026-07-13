public class zxing_0086 {

      static int getNumberOfC40WordsRefactored(Input input, int from, boolean c40,int[] characterLength) {
        int thirdsCount = 0;
        for (int i = from; i < input.length(); i++) {
          if (input.isECI(i)) {
            characterLength[0] = 0;
            return 0;
          }
          char ci = input.charAt(i);
          if (c40 && HighLevelEncoder.isNativeC40(ci) || !c40 && HighLevelEncoder.isNativeText(ci)) {
            thirdsCount++; //native
          } else if (!isExtendedASCII(ci, input.getFNC1Character())) {
            thirdsCount += 2; //shift
          } else {
            int asciiValue = ci & 0xff;
            if (asciiValue >= 128 && (c40 && HighLevelEncoder.isNativeC40((char) (asciiValue - 128)) ||
                                      !c40 && HighLevelEncoder.isNativeText((char) (asciiValue - 128)))) {
              thirdsCount += 3; // shift, Upper shift
            } else {
              thirdsCount += 4; // shift, Upper shift, shift
            }
          }
    
          if (thirdsCount % 3 == 0 || ((thirdsCount - 2) % 3 == 0 && i + 1 == input.length())) {
            characterLength[0] = i - from + 1;
            return (int) Math.ceil(((double) thirdsCount) / 3.0);
          }
        }
        characterLength[0] = 0;
        return 0;
      }
}
