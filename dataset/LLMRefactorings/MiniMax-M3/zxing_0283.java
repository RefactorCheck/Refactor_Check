public class zxing_0283 {

      private void storeRow(int rowNumber) {
        int insertPos = 0;
        while (insertPos < this.rows.size() && this.rows.get(insertPos).getRowNumber() <= rowNumber) {
          insertPos++;
        }
        if (isDuplicateRow(insertPos)) {
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

      private boolean isDuplicateRow(int insertPos) {
        if (insertPos < this.rows.size() && this.rows.get(insertPos).isEquivalent(this.pairs)) {
          return true;
        }
        if (insertPos > 0 && this.rows.get(insertPos - 1).isEquivalent(this.pairs)) {
          return true;
        }
        return false;
      }
}
