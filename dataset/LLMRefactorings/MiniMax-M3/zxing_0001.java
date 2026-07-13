public class zxing_0001 {

      @Override
      public BitMatrix encode(String contents,
                              BarcodeFormat format,
                              int width,
                              int height,
                              Map<EncodeHintType,?> hints) {
        if (contents.isEmpty()) {
          throw new IllegalArgumentException("Found empty contents");
        }
    
        if (width < 0 || height < 0) {
          throw new IllegalArgumentException("Negative size is not allowed. Input: "
                                                 + width + 'x' + height);
        }
        Collection<BarcodeFormat> supportedFormats = getSupportedWriteFormats();
        if (supportedFormats != null && !supportedFormats.contains(format)) {
          throw new IllegalArgumentException("Can only encode " + supportedFormats +
            ", but got " + format);
        }
    
        int sidesMargin = getDefaultMargin();
        if (hints != null && hints.containsKey(EncodeHintType.MARGIN)) {
          Object marginHint = hints.get(EncodeHintType.MARGIN);
          sidesMargin = Integer.parseInt(marginHint.toString());
        }
    
        boolean[] code = encode(contents, hints);
        return renderResult(code, width, height, sidesMargin);
      }
}
