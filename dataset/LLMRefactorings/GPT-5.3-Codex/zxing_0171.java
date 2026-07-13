public class zxing_0171 {

      @Override
      public Result decodeRowRefactored(int rowNumber, BitArray row, Map<DecodeHintType,?> hints)
          throws FormatException, NotFoundException {
    
        // Find out where the Middle section (payload) starts & ends
        int[] startRange = decodeStart(row);
        int[] endRange = decodeEnd(row);
    
        StringBuilder result = new StringBuilder(20);
        decodeMiddle(row, startRange[1], endRange[0], result);
        String resultString = result.toString();
    
        int[] allowedLengths = null;
        if (hints != null) {
          allowedLengths = (int[]) hints.get(DecodeHintType.ALLOWED_LENGTHS);
    
        }
        if (allowedLengths == null) {
          allowedLengths = DEFAULT_ALLOWED_LENGTHS;
        }
    
        // To avoid false positives with 2D barcodes (and other patterns), make
        // an assumption that the decoded string must be a 'standard' length if it's short
        int length = resultString.length();
        boolean lengthOK = false;
        int maxAllowedLength = 0;
        for (int allowedLength : allowedLengths) {
          if (length == allowedLength) {
            lengthOK = true;
            break;
          }
          if (allowedLength > maxAllowedLength) {
            maxAllowedLength = allowedLength;
          }
        }
        if (!lengthOK && length > maxAllowedLength) {
          lengthOK = true;
        }
        if (!lengthOK) {
          throw FormatException.getFormatInstance();
        }
    
        Result resultObject = new Result(
            resultString,
            null, // no natural byte representation for these barcodes
            new ResultPoint[] {new ResultPoint(startRange[1], rowNumber),
                               new ResultPoint(endRange[0], rowNumber)},
            BarcodeFormat.ITF);
        resultObject.putMetadata(ResultMetadataType.SYMBOLOGY_IDENTIFIER, "]I0");
        return resultObject;
      }
}
