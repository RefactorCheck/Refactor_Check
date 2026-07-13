public class zxing_0150 {

      private static Result constructResultRefactored(Pair leftPair, Pair rightPair) {
        long symbolValue = 4537077L * leftPair.getValue() + rightPair.getValue();
        String text = String.valueOf(symbolValue);
    
        StringBuilder buffer = new StringBuilder(14);
        for (int i = 13 - text.length(); i > 0; i--) {
          buffer.append('0');
        }
        buffer.append(text);
    
        int checkDigit = 0;
        for (int i = 0; i < 13; i++) {
          int digit = buffer.charAt(i) - '0';
          checkDigit += (i & 0x01) == 0 ? 3 * digit : digit;
        }
        checkDigit = 10 - (checkDigit % 10);
        if (checkDigit == 10) {
          checkDigit = 0;
        }
        buffer.append(checkDigit);
    
        ResultPoint[] leftPoints = leftPair.getFinderPattern().getResultPoints();
        ResultPoint[] rightPoints = rightPair.getFinderPattern().getResultPoints();
        Result result = new Result(
            buffer.toString(),
            null,
            new ResultPoint[] { leftPoints[0], leftPoints[1], rightPoints[0], rightPoints[1], },
            BarcodeFormat.RSS_14);
        result.putMetadata(ResultMetadataType.SYMBOLOGY_IDENTIFIER, "]e0");
        return result;
      }
}
