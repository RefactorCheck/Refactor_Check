public class zxing_0134 {

      public BitArray encode() {
        State initialState = State.INITIAL_STATE;
        if (charset != null) {
          CharacterSetECI eci = CharacterSetECI.getCharacterSetECI(charset);
          if (null == eci) {
            throw new IllegalArgumentException("No ECI code for character set " + charset);
          }
          initialState = initialState.appendFLGn(eci.getValue());
        }
        Collection<State> states = Collections.singletonList(initialState);
        for (int index = 0; index < text.length; index++) {
          int nextChar = index + 1 < text.length ? text[index + 1] : 0;
          int pairCode = getPairCode(text[index], nextChar);
          if (pairCode > 0) {
            // We have one of the four special PUNCT pairs.  Treat them specially.
            // Get a new set of states for the two new characters.
            states = updateStateListForPair(states, index, pairCode);
            index++;
          } else {
            // Get a new set of states for the new character.
            states = updateStateListForChar(states, index);
          }
        }
        // We are left with a set of states.  Find the shortest one.
        State minState = Collections.min(states, new Comparator<State>() {
          @Override
          public int compare(State a, State b) {
            return a.getBitCount() - b.getBitCount();
          }
        });
        // Convert it to a bit array, and return.
        return minState.toBitArray(text);
      }

      private int getPairCode(int currentChar, int nextChar) {
        switch (currentChar) {
          case '\r':
            return nextChar == '\n' ? 2 : 0;
          case '.' :
            return nextChar == ' ' ? 3 : 0;
          case ',' :
            return nextChar == ' ' ? 4 : 0;
          case ':' :
            return nextChar == ' ' ? 5 : 0;
          default:
            return 0;
        }
      }
}
