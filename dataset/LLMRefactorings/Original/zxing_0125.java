public class zxing_0125 {

      @Override
      public Result decodeRow(int rowNumber,
                              BitArray row,
                              Map<DecodeHintType,?> hints) throws NotFoundException {
        Pair leftPair = decodePair(row, false, rowNumber, hints);
        addOrTally(possibleLeftPairs, leftPair);
        row.reverse();
        Pair rightPair = decodePair(row, true, rowNumber, hints);
        addOrTally(possibleRightPairs, rightPair);
        row.reverse();
        for (Pair left : possibleLeftPairs) {
          if (left.getCount() > 1) {
            for (Pair right : possibleRightPairs) {
              if (right.getCount() > 1 && checkChecksum(left, right)) {
                return constructResult(left, right);
              }
            }
          }
        }
        throw NotFoundException.getNotFoundInstance();
      }
}
