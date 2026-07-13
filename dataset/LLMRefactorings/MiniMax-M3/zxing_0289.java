public class zxing_0289 {

        Mode getEndMode() {
          if (mode == Mode.EDF) {
            if (characterLength < 4) {
              return Mode.ASCII;
            }
            int lastASCII = getLastASCII();
            if (lastASCII > 0 && getCodewordsRemaining(cachedTotalSize + lastASCII) <= 2 - lastASCII) {
              return Mode.ASCII;
            }
          }
          if (shouldSwitchToAscii()) {
            return Mode.ASCII;
          }
          return mode;
        }

        private boolean shouldSwitchToAscii() {
          if (mode == Mode.C40 ||
              mode == Mode.TEXT ||
              mode == Mode.X12) {
            if (fromPosition + characterLength >= input.length() && getCodewordsRemaining(cachedTotalSize) == 0) {
              return true;
            }
            int lastASCII = getLastASCII();
            if (lastASCII == 1 && getCodewordsRemaining(cachedTotalSize + 1) == 0) {
              return true;
            }
          }
          return false;
        }
}
