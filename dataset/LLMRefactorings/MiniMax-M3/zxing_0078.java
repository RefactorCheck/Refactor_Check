public class zxing_0078 {

      @Override
      public BitMatrix encode(String contents,
                              BarcodeFormat format,
                              int width, int height,
                              Map<EncodeHintType,?> hints) throws WriterException {
        return getWriter(format).encode(contents, format, width, height, hints);
      }

      private Writer getWriter(BarcodeFormat format) {
        switch (format) {
          case EAN_8:
            return new EAN8Writer();
          case UPC_E:
            return new UPCEWriter();
          case EAN_13:
            return new EAN13Writer();
          case UPC_A:
            return new UPCAWriter();
          case QR_CODE:
            return new QRCodeWriter();
          case CODE_39:
            return new Code39Writer();
          case CODE_93:
            return new Code93Writer();
          case CODE_128:
            return new Code128Writer();
          case ITF:
            return new ITFWriter();
          case PDF_417:
            return new PDF417Writer();
          case CODABAR:
            return new CodaBarWriter();
          case DATA_MATRIX:
            return new DataMatrixWriter();
          case AZTEC:
            return new AztecWriter();
          default:
            throw new IllegalArgumentException("No encoder available for format " + format);
        }
      }
}
