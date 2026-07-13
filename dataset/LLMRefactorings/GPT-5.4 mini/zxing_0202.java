public class zxing_0202 {

      Map<DecodeHintType,?> buildHints() {
        List<BarcodeFormat> finalPossibleFormatsRefactored = possibleFormats;
        if (finalPossibleFormatsRefactored == null || finalPossibleFormatsRefactored.isEmpty()) {
          finalPossibleFormatsRefactored = new ArrayList<>(Arrays.asList(
              BarcodeFormat.UPC_A,
              BarcodeFormat.UPC_E,
              BarcodeFormat.EAN_13,
              BarcodeFormat.EAN_8,
              BarcodeFormat.RSS_14,
              BarcodeFormat.RSS_EXPANDED
          ));
          if (!productsOnly) {
            finalPossibleFormatsRefactored.addAll(Arrays.asList(
                BarcodeFormat.CODE_39,
                BarcodeFormat.CODE_93,
                BarcodeFormat.CODE_128,
                BarcodeFormat.ITF,
                BarcodeFormat.QR_CODE,
                BarcodeFormat.DATA_MATRIX,
                BarcodeFormat.AZTEC,
                BarcodeFormat.PDF_417,
                BarcodeFormat.CODABAR,
                BarcodeFormat.MAXICODE
            ));
          }
        }
    
        Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
        hints.put(DecodeHintType.POSSIBLE_FORMATS, finalPossibleFormatsRefactored);
        if (tryHarder) {
          hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        }
        if (pureBarcode) {
          hints.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
        }
        return Collections.unmodifiableMap(hints);
      }
}
