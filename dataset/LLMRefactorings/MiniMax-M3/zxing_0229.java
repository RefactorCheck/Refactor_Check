public class zxing_0229 {

      private void updateStateForChar(State state, int index, Collection<State> result) {
        char ch = (char) (text[index] & 0xFF);
        boolean charInCurrentTable = CHAR_MAP[state.getMode()][ch] > 0;
        State stateNoBinary = null;
        for (int mode = 0; mode <= MODE_PUNCT; mode++) {
          int charInMode = CHAR_MAP[mode][ch];
          if (charInMode > 0) {
            if (stateNoBinary == null) {
              stateNoBinary = state.endBinaryShift(index);
            }
            applyModeTransition(state, stateNoBinary, mode, charInMode, charInCurrentTable, result);
          }
        }
        if (state.getBinaryShiftByteCount() > 0 || CHAR_MAP[state.getMode()][ch] == 0) {
          State binaryState = state.addBinaryShiftChar(index);
          result.add(binaryState);
        }
      }

      private void applyModeTransition(State state, State stateNoBinary, int mode, int charInMode,
                                       boolean charInCurrentTable, Collection<State> result) {
        if (!charInCurrentTable || mode == state.getMode() || mode == MODE_DIGIT) {
          State latchState = stateNoBinary.latchAndAppend(mode, charInMode);
          result.add(latchState);
        }
        if (!charInCurrentTable && SHIFT_TABLE[state.getMode()][mode] >= 0) {
          State shiftState = stateNoBinary.shiftAndAppend(mode, charInMode);
          result.add(shiftState);
        }
      }
}
