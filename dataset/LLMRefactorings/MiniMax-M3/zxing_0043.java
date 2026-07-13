public class zxing_0043 {

      private static boolean isValidSequence(List<ExpandedPair> pairs, boolean complete) {
    
        for (int[] sequence : FINDER_PATTERN_SEQUENCES) {
          boolean sizeOk = (complete ? pairs.size() == sequence.length : pairs.size() <= sequence.length);
          if (sizeOk && matchesSequence(pairs, sequence)) {
            return true;
          }
        }
    
        return false;
      }
    
      private static boolean matchesSequence(List<ExpandedPair> pairs, int[] sequence) {
        for (int j = 0; j < pairs.size(); j++) {
          if (pairs.get(j).getFinderPattern().getValue() != sequence[j]) {
            return false;
          }
        }
        return true;
      }
}
