public class zxing_0162 {

      static void addEdges(String stringToEncode,
                           ECIEncoderSet encoderSet,
                           InputEdge[][] edges,
                           int from,
                           InputEdge previous,
                           int fnc1) {
    
        char ch = stringToEncode.charAt(from);
        boolean isFnc1 = ch == fnc1;
    
        int start = 0;
        int end = encoderSet.length();
        if (encoderSet.getPriorityEncoderIndex() >= 0 && (isFnc1 || encoderSet.canEncode(ch,
            encoderSet.getPriorityEncoderIndex()))) {
          start = encoderSet.getPriorityEncoderIndex();
          end = start + 1;
        }
    
        for (int i = start; i < end; i++) {
          if (isFnc1 || encoderSet.canEncode(ch,i)) {
            addEdge(edges, from + 1, new InputEdge(ch, encoderSet, i, previous, fnc1));
          }
        }
      }
}
