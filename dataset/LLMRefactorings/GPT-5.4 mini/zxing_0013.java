public class zxing_0013 {

      private static boolean isPartialRow(Iterable<ExpandedPair> pairs, Iterable<ExpandedRow> rows) {
        for (ExpandedRow r : rows) {
          boolean allFoundRefactored = true;
          for (ExpandedPair p : pairs) {
            boolean found = false;
            for (ExpandedPair pp : r.getPairs()) {
              if (p.equals(pp)) {
                found = true;
                break;
              }
            }
            if (!found) {
              allFoundRefactored = false;
              break;
            }
          }
          if (allFoundRefactored) {
            // the row 'r' contain all the pairs from 'pairs'
            return true;
          }
        }
        return false;
      }
}
