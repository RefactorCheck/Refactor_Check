public class zxing_0283 {

      private void storeRow(int rowNumber) {
        // Discard if duplicate above or below; otherwise insert in order by row number.
        int insertPos = 0;
        boolean prevIsSame = false;
        boolean nextIsSame = false;
        while (insertPos < this.rows.size()) {
          ExpandedRow erow = this.rows.get(insertPos);
          if (erow.getRowNumber() > rowNumber) {
            nextIsSame = erow.isEquivalent(this.pairs);
            break;
          }
          prevIsSame = erow.isEquivalent(this.pairs);
          insertPos++;
        }
        if (nextIsSame || prevIsSame) {
          return;
        }
    
        // When the row was partially decoded (e.g. 2 pairs found instead of 3),
        // it will prevent us from detecting the barcode.
        // Try to merge partial rows
    
        // Check whether the row is part of an already detected row
        if (isPartialRow(this.pairs, this.rows)) {
          return;
        }
    
        this.rows.add(insertPos, new ExpandedRow(this.pairs, rowNumber));
    
        removePartialRows(this.pairs, this.rows);
      }
}
