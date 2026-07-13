public class zxing_0114 {

      static int[] encodeMinimally(String stringToEncode, ECIEncoderSet encoderSet, int fnc1) {
        int inputLength = stringToEncode.length();
    
        // Array that represents vertices. There is a vertex for every character and encoding.
        InputEdge[][] edges = new InputEdge[inputLength + 1][encoderSet.length()];
        addEdges(stringToEncode, encoderSet, edges, 0, null, fnc1);
    
        for (int i = 1; i <= inputLength; i++) {
          for (int j = 0; j < encoderSet.length(); j++) {
            if (edges[i][j] != null && i < inputLength) {
              addEdges(stringToEncode, encoderSet, edges, i, edges[i][j], fnc1);
            }
          }
          //optimize memory by removing edges that have been passed.
          for (int j = 0; j < encoderSet.length(); j++) {
            edges[i - 1][j] = null;
          }
        }
        int minimalJ = -1;
        int minimalSize = Integer.MAX_VALUE;
        for (int j = 0; j < encoderSet.length(); j++) {
          if (edges[inputLength][j] != null) {
            InputEdge edge = edges[inputLength][j];
            if (edge.cachedTotalSize < minimalSize) {
              minimalSize = edge.cachedTotalSize;
              minimalJ = j;
            }
          }
        }
        if (minimalJ < 0) {
          throw new IllegalStateException("Failed to encode \"" + stringToEncode + "\"");
        }
        List<Integer> intsAL = buildResultList(edges[inputLength][minimalJ], encoderSet);
        int[] ints = new int[intsAL.size()];
        for (int i = 0; i < ints.length; i++) {
          ints[i] = intsAL.get(i);
        }
        return ints;
      }

      private static List<Integer> buildResultList(InputEdge startEdge, ECIEncoderSet encoderSet) {
        List<Integer> intsAL = new ArrayList<>();
        InputEdge current = startEdge;
        while (current != null) {
          if (current.isFNC1()) {
            intsAL.add(0, 1000);
          } else {
            byte[] bytes = encoderSet.encode(current.c, current.encoderIndex);
            for (int i = bytes.length - 1; i >= 0; i--) {
              intsAL.add(0, (bytes[i] & 0xFF));
            }
          }
          int previousEncoderIndex = current.previous == null ? 0 : current.previous.encoderIndex;
          if (previousEncoderIndex != current.encoderIndex) {
            intsAL.add(0, 256 + encoderSet.getECIValue(current.encoderIndex));
          }
          current = current.previous;
        }
        return intsAL;
      }
}
