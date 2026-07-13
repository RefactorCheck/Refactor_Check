public class zxing_0282 {

      static void addEdges(Input input, Edge[][] edges, int from, Edge previous) {
    
        if (input.isECI(from)) {
          addEdge(edges, new Edge(input, Mode.ASCII, from, 1, previous));
          return;
        }
    
        char ch = input.charAt(from);
        if (previous == null || previous.getEndMode() != Mode.EDF) { //not possible to unlatch a full EDF edge to something
                                                                     //else
          if (HighLevelEncoder.isDigit(ch) && input.haveNCharacters(from, 2) &&
              HighLevelEncoder.isDigit(input.charAt(from + 1))) {
            // two digits ASCII encoded
            addEdge(edges, new Edge(input, Mode.ASCII, from, 2, previous));
          } else {
            // one ASCII encoded character or an extended character via Upper Shift
            addEdge(edges, new Edge(input, Mode.ASCII, from, 1, previous));
          }
    
          Mode[] modes = {Mode.C40, Mode.TEXT};
          for (Mode mode : modes) {
            int[] characterLength = new int[1];
            if (getNumberOfC40Words(input, from, mode == Mode.C40, characterLength) > 0) {
              addEdge(edges, new Edge(input, mode, from, characterLength[0], previous));
            }
          }
    
          if (input.haveNCharacters(from,3) &&
              HighLevelEncoder.isNativeX12(input.charAt(from)) &&
              HighLevelEncoder.isNativeX12(input.charAt(from + 1)) &&
              HighLevelEncoder.isNativeX12(input.charAt(from + 2))) {
            addEdge(edges, new Edge(input, Mode.X12, from, 3, previous));
          }
    
          addEdge(edges, new Edge(input, Mode.B256, from, 1, previous));
        }
    
        //We create 4 EDF edges,  with 1, 2 3 or 4 characters length. The fourth normally doesn't have a latch to ASCII
        //unless it is 2 characters away from the end of the input.
        int i;
        for (i = 0; i < 3; i++) {
          int pos = from + i;
          if (input.haveNCharacters(pos,1) && HighLevelEncoder.isNativeEDIFACT(input.charAt(pos))) {
            addEdge(edges, new Edge(input, Mode.EDF, from, i + 1, previous));
          } else {
            break;
          }
        }
        if (i == 3 && input.haveNCharacters(from, 4) && HighLevelEncoder.isNativeEDIFACT(input.charAt(from + 3))) {
          addEdge(edges, new Edge(input, Mode.EDF, from, 4, previous));
        }
      }
}
