public class zxing_0221 {

      @Override
      public BitMatrix encode(String contents,
                              BarcodeFormat format,
                              int width,
                              int height,
                              Map<EncodeHintType,?> hints) throws WriterException {

        validateInput(contents, format, width, height);

        ErrorCorrectionLevel errorCorrectionLevel = ErrorCorrectionLevel.L;
        int quietZone = QUIET_ZONE_SIZE;
        if (hints != null) {
          if (hints.containsKey(EncodeHintType.ERROR_CORRECTION)) {
            errorCorrectionLevel = ErrorCorrectionLevel.valueOf(hints.get(EncodeHintType.ERROR_CORRECTION).toString());
          }
          if (hints.containsKey(EncodeHintType.MARGIN)) {
            quietZone = Integer.parseInt(hints.get(EncodeHintType.MARGIN).toString());
          }
        }

        QRCode code = Encoder.encode(contents, errorCorrectionLevel, hints);
        return renderResult(code, width, height, quietZone);
      }

      private void validateInput(String contents, BarcodeFormat format, int width, int height) {
        if (contents.isEmpty()) {
          throw new IllegalArgumentException("Found empty contents");
        }

        if (format != BarcodeFormat.QR_CODE) {
          throw new IllegalArgumentException("Can only encode QR_CODE, but got " + format);
        }

        if (width < 0 || height < 0) {
          throw new IllegalArgumentException("Requested dimensions are too small: " + width + 'x' +
              height);
        }
      }
}
