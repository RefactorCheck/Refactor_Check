public class zxing_0043 {

      private static boolean isValidSequence(List<ExpandedPair> pairs, boolean complete) {
    
        for (int[] sequence : FINDER_PATTERN_SEQUENCES) {
          boolean sizeOk = (complete ? pairs.size() == sequence.length : pairs.size() <= sequence.length);
          if (sizeOk) {
            boolean stop = true;
            for (int j = 0; j < pairs.size(); j++) {
              if (pairs.get(j).getFinderPattern().getValue() != sequence[j]) {
                stop = false;
                break;
              }
            }
            if (stop) {
              return true;
            }
          }
        }
    
        return false;
      }
}
