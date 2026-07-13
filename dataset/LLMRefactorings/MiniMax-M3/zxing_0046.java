public class zxing_0046 {

        byte[] getC40Words(boolean c40, int fnc1) {
          List<Byte> c40Values = new ArrayList<>();
          for (int i = 0; i < characterLength; i++) {
            char ci = input.charAt(fromPosition + i);
            if (c40 && HighLevelEncoder.isNativeC40(ci) || !c40 && HighLevelEncoder.isNativeText(ci)) {
              c40Values.add((byte) getC40Value(c40, 0, ci, fnc1));
            } else if (!isExtendedASCII(ci, fnc1)) {
              int shiftValue = getShiftValue(ci, c40, fnc1);
              c40Values.add((byte) shiftValue); //Shift[123]
              c40Values.add((byte) getC40Value(c40, shiftValue, ci, fnc1));
            } else {
              addExtendedAsciiBytes(c40Values, ci, c40, fnc1);
            }
          }

          if ((c40Values.size() % 3) != 0) {
            assert (c40Values.size() - 2) % 3 == 0 && fromPosition + characterLength == input.length();
            c40Values.add((byte) 0); // pad with 0 (Shift 1)
          }

          byte[] result = new byte[c40Values.size() / 3 * 2];
          int byteIndex = 0;
          for (int i = 0; i < c40Values.size(); i += 3) {
            setC40Word(result,byteIndex, c40Values.get(i) & 0xff, c40Values.get(i + 1) & 0xff, c40Values.get(i + 2) & 0xff);
            byteIndex += 2;
          }
          return result;
        }

        private void addExtendedAsciiBytes(List<Byte> c40Values, char ci, boolean c40, int fnc1) {
          char asciiValue = (char) ((ci & 0xff) - 128);
          c40Values.add((byte) 1); //Shift 2
          c40Values.add((byte) 30); //Upper Shift
          if (c40 && HighLevelEncoder.isNativeC40(asciiValue) ||
              !c40 && HighLevelEncoder.isNativeText(asciiValue)) {
            c40Values.add((byte) getC40Value(c40, 0, asciiValue, fnc1));
          } else {
            int shiftValue = getShiftValue(asciiValue, c40, fnc1);
            c40Values.add((byte) shiftValue); // Shift[123]
            c40Values.add((byte) getC40Value(c40, shiftValue, asciiValue, fnc1));
          }
        }
}
