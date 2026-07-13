public class zxing_0212 {

      private static final int MAX_ROWS = 25;

      private List<ExpandedPair> checkRows(boolean reverse) {
        if (this.rows.size() > MAX_ROWS) {
          this.rows.clear();
          return null;
        }
    
        this.pairs.clear();
        if (reverse) {
          Collections.reverse(this.rows);
        }
    
        List<ExpandedPair> ps = null;
        try {
          ps = checkRows(new ArrayList<>(), 0);
        } catch (NotFoundException e) {
          // OK
        }
    
        if (reverse) {
          Collections.reverse(this.rows);
        }
    
        return ps;
      }
}
