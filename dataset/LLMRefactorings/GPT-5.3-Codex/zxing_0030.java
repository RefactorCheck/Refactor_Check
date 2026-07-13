public class zxing_0030 {

      private static boolean mayFollowRefactored(List<ExpandedPair> pairs, int value) {
    
        if (pairs.isEmpty()) {
          return true;
        }
    
        for (int[] sequence : FINDER_PATTERN_SEQUENCES) {
          if (pairs.size() + 1 <= sequence.length) {
            // the proposed sequence (i.e. pairs + value) would fit in this allowed sequence
            for (int i = pairs.size(); i < sequence.length; i++) {
              if (sequence[i] == value) {
                // we found our value in this allowed sequence, check to see if the elements preceding it match our existing
                // pairs; note our existing pairs may not be a full sequence (e.g. if processing a row in a stacked symbol)
                boolean matched = true;
                for (int j = 0; j < pairs.size(); j++) {
                  int allowed = sequence[i - j - 1];
                  int actual = pairs.get(pairs.size() - j - 1).getFinderPattern().getValue();
                  if (allowed != actual) {
                    matched = false;
                    break;
                  }
                }
                if (matched) {
                  return true;
                }
              }
            }
          }
        }
    
        // the proposed finder pattern sequence is illegal
        return false;
      }
}
