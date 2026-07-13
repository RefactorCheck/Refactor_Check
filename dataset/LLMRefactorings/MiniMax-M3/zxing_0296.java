public class zxing_0296 {

      private static final int MODULES_IN_RSS = 1597;
      private static final int CHECKSUM_WEIGHT = 4;

      private Pair decodePair(BitArray row, boolean right, int rowNumber, Map<DecodeHintType,?> hints) {
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
          return new Pair(MODULES_IN_RSS * outside.getValue() + inside.getValue(),
                          outside.getChecksumPortion() + CHECKSUM_WEIGHT * inside.getChecksumPortion(),
                          pattern);
        } catch (NotFoundException ignored) {
          return null;
        }
      }
}
