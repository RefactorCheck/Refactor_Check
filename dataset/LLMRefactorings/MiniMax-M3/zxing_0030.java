public class zxing_0030 {

      private static boolean mayFollow(List<ExpandedPair> pairs, int value) {
    
        if (pairs.isEmpty()) {
          return true;
        }
    
        for (int[] sequence : FINDER_PATTERN_SEQUENCES) {
          if (pairs.size() + 1 <= sequence.length) {
            for (int i = pairs.size(); i < sequence.length; i++) {
              if (sequence[i] == value) {
                if (matchesExistingPairs(sequence, i, pairs)) {
                  return true;
                }
              }
            }
          }
        }
    
        return false;
      }
      
      private static boolean matchesExistingPairs(int[] sequence, int valueIndex, List<ExpandedPair> pairs) {
        for (int j = 0; j < pairs.size(); j++) {
          int allowed = sequence[valueIndex - j - 1];
          int actual = pairs.get(pairs.size() - j - 1).getFinderPattern().getValue();
          if (allowed != actual) {
            return false;
          }
        }
        return true;
      }
}
