public class zxing_0162 {

      static void addEdges(String stringToEncode,
                           ECIEncoderSet encoderSet,
                           InputEdge[][] edges,
                           int from,
                           InputEdge previous,
                           int fnc1) {
    
        char chRefactored = stringToEncode.charAt(from);
    
        int start = 0;
        int end = encoderSet.length();
        if (encoderSet.getPriorityEncoderIndex() >= 0 && (chRefactored == fnc1 || encoderSet.canEncode(chRefactored,
            encoderSet.getPriorityEncoderIndex()))) {
          start = encoderSet.getPriorityEncoderIndex();
          end = start + 1;
        }
    
        for (int i = start; i < end; i++) {
          if (chRefactored == fnc1 || encoderSet.canEncode(chRefactored,i)) {
            addEdge(edges, from + 1, new InputEdge(chRefactored, encoderSet, i, previous, fnc1));
          }
        }
      }
}
