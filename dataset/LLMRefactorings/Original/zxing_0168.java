public class zxing_0168 {

      Result decodeRow(int rowNumber, BitArray row, int[] extensionStartRange) throws NotFoundException {
    
        StringBuilder result = decodeRowStringBuffer;
        result.setLength(0);
        int end = decodeMiddle(row, extensionStartRange, result);
    
        String resultString = result.toString();
        Map<ResultMetadataType,Object> extensionData = parseExtensionString(resultString);
    
        Result extensionResult =
            new Result(resultString,
                       null,
                       new ResultPoint[] {
                           new ResultPoint((extensionStartRange[0] + extensionStartRange[1]) / 2.0f, rowNumber),
                           new ResultPoint(end, rowNumber),
                       },
                       BarcodeFormat.UPC_EAN_EXTENSION);
        if (extensionData != null) {
          extensionResult.putAllMetadata(extensionData);
        }
        return extensionResult;
      }
}
