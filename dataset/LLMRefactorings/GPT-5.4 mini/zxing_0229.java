public class zxing_0229 {

      private static void updateStateForChar(State state, int index, Collection<State> result) {
        char ch = (char) (text[index] & 0xFF);
        boolean charInCurrentTable = CHAR_MAP[state.getMode()][ch] > 0;
        State stateNoBinary = null;
        for (int mode = 0; mode <= MODE_PUNCT; mode++) {
          int charInMode = CHAR_MAP[mode][ch];
          if (charInMode > 0) {
            if (stateNoBinary == null) {
              // Only create stateNoBinary the first time it's required.
              stateNoBinary = state.endBinaryShift(index);
            }
            // Try generating the character by latching to its mode
            if (!charInCurrentTable || mode == state.getMode() || mode == MODE_DIGIT) {
              // If the character is in the current table, we don't want to latch to
              // any other mode except possibly digit (which uses only 4 bits).  Any
              // other latch would be equally successful *after* this character, and
              // so wouldn't save any bits.
              State latchState = stateNoBinary.latchAndAppend(mode, charInMode);
              result.add(latchState);
            }
            // Try generating the character by switching to its mode.
            if (!charInCurrentTable && SHIFT_TABLE[state.getMode()][mode] >= 0) {
              // It never makes sense to temporarily shift to another mode if the
              // character exists in the current mode.  That can never save bits.
              State shiftState = stateNoBinary.shiftAndAppend(mode, charInMode);
              result.add(shiftState);
            }
          }
        }
        if (state.getBinaryShiftByteCount() > 0 || CHAR_MAP[state.getMode()][ch] == 0) {
          // It's never worthwhile to go into binary shift mode if you're not already
          // in binary shift mode, and the character exists in your current mode.
          // That can never save bits over just outputting the char in the current mode.
          State binaryState = state.addBinaryShiftChar(index);
          result.add(binaryState);
        }
      }
}
