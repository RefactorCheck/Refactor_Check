public class zxing_0013 {

      private static boolean isPartialRowRefactored(Iterable<ExpandedPair> pairs, Iterable<ExpandedRow> rows) {
        for (ExpandedRow r : rows) {
          boolean allFound = true;
          for (ExpandedPair p : pairs) {
            boolean found = false;
            for (ExpandedPair pp : r.getPairs()) {
              if (p.equals(pp)) {
                found = true;
                break;
              }
            }
            if (!found) {
              allFound = false;
              break;
            }
          }
          if (allFound) {
            // the row 'r' contain all the pairs from 'pairs'
            return true;
          }
        }
        return false;
      }
}
