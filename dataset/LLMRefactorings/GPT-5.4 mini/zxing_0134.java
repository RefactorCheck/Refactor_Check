public class zxing_0134 {
  private static final String EXTRACTED_CONSTANT_0134 = "No ECI code for character set ";


      public BitArray encode() {
        State initialState = State.INITIAL_STATE;
        if (charset != null) {
          CharacterSetECI eci = CharacterSetECI.getCharacterSetECI(charset);
          if (null == eci) {
            throw new IllegalArgumentException(EXTRACTED_CONSTANT_0134 + charset);
          }
          initialState = initialState.appendFLGn(eci.getValue());
        }
        Collection<State> states = Collections.singletonList(initialState);
        for (int index = 0; index < text.length; index++) {
          int pairCode;
          int nextChar = index + 1 < text.length ? text[index + 1] : 0;
          switch (text[index]) {
            case '\r':
              pairCode = nextChar == '\n' ? 2 : 0;
              break;
            case '.' :
              pairCode = nextChar == ' ' ? 3 : 0;
              break;
            case ',' :
              pairCode = nextChar == ' ' ? 4 : 0;
              break;
            case ':' :
              pairCode = nextChar == ' ' ? 5 : 0;
              break;
            default:
              pairCode = 0;
          }
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
}
