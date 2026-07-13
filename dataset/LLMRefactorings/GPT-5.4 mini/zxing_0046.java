public class zxing_0046 {

        byte[] getC40Words(boolean c40, int fnc1) {
          List<Byte> c40ValuesRefactored = new ArrayList<>();
          for (int i = 0; i < characterLength; i++) {
            char ci = input.charAt(fromPosition + i);
            if (c40 && HighLevelEncoder.isNativeC40(ci) || !c40 && HighLevelEncoder.isNativeText(ci)) {
              c40ValuesRefactored.add((byte) getC40Value(c40, 0, ci, fnc1));
            } else if (!isExtendedASCII(ci, fnc1)) {
              int shiftValue = getShiftValue(ci, c40, fnc1);
              c40ValuesRefactored.add((byte) shiftValue); //Shift[123]
              c40ValuesRefactored.add((byte) getC40Value(c40, shiftValue, ci, fnc1));
            } else {
              char asciiValue = (char) ((ci & 0xff) - 128);
              if (c40 && HighLevelEncoder.isNativeC40(asciiValue) ||
                  !c40 && HighLevelEncoder.isNativeText(asciiValue)) {
                c40ValuesRefactored.add((byte) 1); //Shift 2
                c40ValuesRefactored.add((byte) 30); //Upper Shift
                c40ValuesRefactored.add((byte) getC40Value(c40, 0, asciiValue, fnc1));
              } else {
                c40ValuesRefactored.add((byte) 1); //Shift 2
                c40ValuesRefactored.add((byte) 30); //Upper Shift
                int shiftValue = getShiftValue(asciiValue, c40, fnc1);
                c40ValuesRefactored.add((byte) shiftValue); // Shift[123]
                c40ValuesRefactored.add((byte) getC40Value(c40, shiftValue, asciiValue, fnc1));
              }
            }
          }
    
          if ((c40ValuesRefactored.size() % 3) != 0) {
            assert (c40ValuesRefactored.size() - 2) % 3 == 0 && fromPosition + characterLength == input.length();
            c40ValuesRefactored.add((byte) 0); // pad with 0 (Shift 1)
          }
    
          byte[] result = new byte[c40ValuesRefactored.size() / 3 * 2];
          int byteIndex = 0;
          for (int i = 0; i < c40ValuesRefactored.size(); i += 3) {
            setC40Word(result,byteIndex, c40ValuesRefactored.get(i) & 0xff, c40ValuesRefactored.get(i + 1) & 0xff, c40ValuesRefactored.get(i + 2) & 0xff);
            byteIndex += 2;
          }
          return result;
        }
}
