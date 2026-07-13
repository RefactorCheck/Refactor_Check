public class zxing_0154 {

      List<ExpandedPair> decodeRow2pairsRefactored(int rowNumber, BitArray row) throws NotFoundException {
        this.pairs.clear();
        boolean done = false;
        while (!done) {
          try {
            this.pairs.add(retrieveNextPair(row, this.pairs, rowNumber));
          } catch (NotFoundException nfe) {
            if (this.pairs.isEmpty()) {
              throw nfe;
            }
            // exit this loop when retrieveNextPair() fails and throws
            done = true;
          }
        }
    
        if (checkChecksum() && isValidSequence(this.pairs, true)) {
          return this.pairs;
        }
    
        boolean tryStackedDecode = !this.rows.isEmpty();
        storeRow(rowNumber); // TODO: deal with reversed rows
        if (tryStackedDecode) {
          // When the image is 180-rotated, then rows are sorted in wrong direction.
          // Try twice with both the directions.
          List<ExpandedPair> ps = checkRows(false);
          if (ps != null) {
            return ps;
          }
          ps = checkRows(true);
          if (ps != null) {
            return ps;
          }
        }
    
        throw NotFoundException.getNotFoundInstance();
      }
}
