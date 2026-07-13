public class zxing_0013 {

      private static boolean isPartialRow(Iterable<ExpandedPair> pairs, Iterable<ExpandedRow> rows) {
        for (ExpandedRow r : rows) {
          if (rowContainsAllPairs(r, pairs)) {
            return true;
          }
        }
        return false;
      }

      private static boolean rowContainsAllPairs(ExpandedRow row, Iterable<ExpandedPair> pairs) {
        for (ExpandedPair p : pairs) {
          boolean found = false;
          for (ExpandedPair pp : row.getPairs()) {
            if (p.equals(pp)) {
              found = true;
              break;
            }
          }
          if (!found) {
            return false;
          }
        }
        return true;
      }
}
