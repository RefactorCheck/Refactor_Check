public class zxing_0169 {

      static void appendBytes(String content,
                              Mode mode,
                              BitArray bits,
                              Charset encoding) throws WriterException {
        switch (mode) {
          case NUMERIC:
            appendNumericBytes(content, bits);
            break;
          case ALPHANUMERIC:
            appendAlphanumericBytes(content, bits);
            break;
          case BYTE:
            append8BitBytes(content, bits, encoding);
            break;
          case KANJI:
            appendKanjiBytes(content, bits);
            break;
          default:
            throw new WriterException("Invalid mode: " + mode);
        }
      }
}
