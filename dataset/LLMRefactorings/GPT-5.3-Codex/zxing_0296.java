public class zxing_0296 {

      private Pair decodePairRefactored(BitArray row, boolean right, int rowNumber, Map<DecodeHintType,?> hints) {
        try {
          int[] startEnd = findFinderPattern(row, right);
          FinderPattern pattern = parseFoundFinderPattern(row, rowNumber, right, startEnd);
    
          ResultPointCallback resultPointCallback = hints == null ? null :
              (ResultPointCallback) hints.get(DecodeHintType.NEED_RESULT_POINT_CALLBACK);
    
          if (resultPointCallback != null) {
            startEnd = pattern.getStartEnd();
            float center = (startEnd[0] + startEnd[1] - 1) / 2.0f;
            if (right) {
              // row is actually reversed
              center = row.getSize() - 1 - center;
            }
            resultPointCallback.foundPossibleResultPoint(new ResultPoint(center, rowNumber));
          }
    
          DataCharacter outside = decodeDataCharacter(row, pattern, true);
          DataCharacter inside = decodeDataCharacter(row, pattern, false);
          return new Pair(1597 * outside.getValue() + inside.getValue(),
                          outside.getChecksumPortion() + 4 * inside.getChecksumPortion(),
                          pattern);
        } catch (NotFoundException ignored) {
          return null;
        }
      }
}
