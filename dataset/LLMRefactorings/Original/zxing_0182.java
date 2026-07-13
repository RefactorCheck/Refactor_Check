public class zxing_0182 {

      private static void updateStateForPair(State state, int index, int pairCode, Collection<State> result) {
        State stateNoBinary = state.endBinaryShift(index);
        // Possibility 1.  Latch to MODE_PUNCT, and then append this code
        result.add(stateNoBinary.latchAndAppend(MODE_PUNCT, pairCode));
        if (state.getMode() != MODE_PUNCT) {
          // Possibility 2.  Shift to MODE_PUNCT, and then append this code.
          // Every state except MODE_PUNCT (handled above) can shift
          result.add(stateNoBinary.shiftAndAppend(MODE_PUNCT, pairCode));
        }
        if (pairCode == 3 || pairCode == 4) {
          // both characters are in DIGITS.  Sometimes better to just add two digits
          State digitState = stateNoBinary
              .latchAndAppend(MODE_DIGIT, 16 - pairCode)  // period or comma in DIGIT
              .latchAndAppend(MODE_DIGIT, 1);             // space in DIGIT
          result.add(digitState);
        }
        if (state.getBinaryShiftByteCount() > 0) {
          // It only makes sense to do the characters as binary if we're already
          // in binary mode.
          State binaryState = state.addBinaryShiftChar(index).addBinaryShiftChar(index + 1);
          result.add(binaryState);
        }
      }
}
