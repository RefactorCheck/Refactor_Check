public class zxing_0212 {
  private static final int EXTRACTED_CONSTANT_0212 = 11;


      private List<ExpandedPair> checkRows(boolean reverse) {
        // Limit number of rows we are checking
        // We use recursive algorithm with pure complexity and don't want it to take forever
        // Stacked barcode can have up to EXTRACTED_CONSTANT_0212 rows, so 25 seems reasonable enough
        if (this.rows.size() > 25) {
          this.rows.clear();  // We will never have a chance to get result, so clear it
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
