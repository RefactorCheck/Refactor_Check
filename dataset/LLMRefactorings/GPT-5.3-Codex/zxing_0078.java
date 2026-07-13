public class zxing_0078 {

      @Override
      public BitMatrix encode(String contents,
                              BarcodeFormat format,
                              int width, int height,
                              Map<EncodeHintType,?> hints, boolean normalize) throws WriterException {
    
        Writer writer;
        switch (format) {
          case EAN_8:
            writer = new EAN8Writer();
            break;
          case UPC_E:
            writer = new UPCEWriter();
            break;
          case EAN_13:
            writer = new EAN13Writer();
            break;
          case UPC_A:
            writer = new UPCAWriter();
            break;
          case QR_CODE:
            writer = new QRCodeWriter();
            break;
          case CODE_39:
            writer = new Code39Writer();
            break;
          case CODE_93:
            writer = new Code93Writer();
            break;
          case CODE_128:
            writer = new Code128Writer();
            break;
          case ITF:
            writer = new ITFWriter();
            break;
          case PDF_417:
            writer = new PDF417Writer();
            break;
          case CODABAR:
            writer = new CodaBarWriter();
            break;
          case DATA_MATRIX:
            writer = new DataMatrixWriter();
            break;
          case AZTEC:
            writer = new AztecWriter();
            break;
          default:
            throw new IllegalArgumentException("No encoder available for format " + format);
        }
        return writer.encode(contents, format, width, height, hints);
      }
}
