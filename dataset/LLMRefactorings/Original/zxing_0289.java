public class zxing_0289 {

        Mode getEndMode() {
          if (mode == Mode.EDF) {
            if (characterLength < 4) {
              return Mode.ASCII;
            }
            int lastASCII = getLastASCII(); // see 5.2.8.2 EDIFACT encodation Rules
            if (lastASCII > 0 && getCodewordsRemaining(cachedTotalSize + lastASCII) <= 2 - lastASCII) {
              return Mode.ASCII;
            }
          }
          if (mode == Mode.C40 ||
              mode == Mode.TEXT ||
              mode == Mode.X12) {
    
            // see 5.2.5.2 C40 encodation rules and 5.2.7.2 ANSI X12 encodation rules
            if (fromPosition + characterLength >= input.length() && getCodewordsRemaining(cachedTotalSize) == 0) {
              return Mode.ASCII;
            }
            int lastASCII = getLastASCII();
            if (lastASCII == 1 && getCodewordsRemaining(cachedTotalSize + 1) == 0) {
              return Mode.ASCII;
            }
          }
          return mode;
        }
}
