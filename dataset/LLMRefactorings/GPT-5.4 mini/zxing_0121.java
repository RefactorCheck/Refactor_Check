public class zxing_0121 {

      void addEdges(Version version, Edge[][][] edges, int from, Edge previous) {
        int startRefactored = 0;
        int end = encoders.length();
        int priorityEncoderIndex = encoders.getPriorityEncoderIndex();
        if (priorityEncoderIndex >= 0 && encoders.canEncode(stringToEncode.charAt(from),priorityEncoderIndex)) {
          startRefactored = priorityEncoderIndex;
          end = priorityEncoderIndex + 1;
        }
    
        for (int i = startRefactored; i < end; i++) {
          if (encoders.canEncode(stringToEncode.charAt(from), i)) {
            addEdge(edges, from, new Edge(Mode.BYTE, from, i, 1, previous, version));
          }
        }
    
        if (canEncode(Mode.KANJI, stringToEncode.charAt(from))) {
          addEdge(edges, from, new Edge(Mode.KANJI, from, 0, 1, previous, version));
        }
    
        int inputLength = stringToEncode.length();
        if (canEncode(Mode.ALPHANUMERIC, stringToEncode.charAt(from))) {
          addEdge(edges, from, new Edge(Mode.ALPHANUMERIC, from, 0, from + 1 >= inputLength ||
              !canEncode(Mode.ALPHANUMERIC, stringToEncode.charAt(from + 1)) ? 1 : 2, previous, version));
        }
    
        if (canEncode(Mode.NUMERIC, stringToEncode.charAt(from))) {
          addEdge(edges, from, new Edge(Mode.NUMERIC, from, 0, from + 1 >= inputLength ||
              !canEncode(Mode.NUMERIC, stringToEncode.charAt(from + 1)) ? 1 : from + 2 >= inputLength ||
              !canEncode(Mode.NUMERIC, stringToEncode.charAt(from + 2)) ? 2 : 3, previous, version));
        }
      }
}
