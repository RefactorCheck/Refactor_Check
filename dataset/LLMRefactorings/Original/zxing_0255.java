public class zxing_0255 {

      private static ChartServletRequestParameters doParseParameters(ServletRequest request, boolean readBody)
          throws IOException {
    
        String chartType = request.getParameter("cht");
        Preconditions.checkArgument(chartType == null || "qr".equals(chartType), "Bad type");
    
        String widthXHeight = request.getParameter("chs");
        Preconditions.checkNotNull(widthXHeight, "No size");
        int xIndex = widthXHeight.indexOf('x');
        Preconditions.checkArgument(xIndex >= 0, "Bad size");
    
        int width = Integer.parseInt(widthXHeight.substring(0, xIndex));
        int height = Integer.parseInt(widthXHeight.substring(xIndex + 1));
        Preconditions.checkArgument(width > 0 && height > 0, "Bad size");
        Preconditions.checkArgument(width <= MAX_DIMENSION && height <= MAX_DIMENSION, "Bad size");
    
        String outputEncodingName = request.getParameter("choe");
        Charset outputEncoding;
        if (outputEncodingName == null) {
          outputEncoding = StandardCharsets.UTF_8;
        } else {
          outputEncoding = Charset.forName(outputEncodingName);
          Preconditions.checkArgument(SUPPORTED_OUTPUT_ENCODINGS.contains(outputEncoding), "Bad output encoding");
        }
    
        ErrorCorrectionLevel ecLevel = ErrorCorrectionLevel.L;
        int margin = 4;
    
        String ldString = request.getParameter("chld");
        if (ldString != null) {
          int pipeIndex = ldString.indexOf('|');
          if (pipeIndex < 0) {
            // Only an EC level
            ecLevel = ErrorCorrectionLevel.valueOf(ldString);
          } else {
            ecLevel = ErrorCorrectionLevel.valueOf(ldString.substring(0, pipeIndex));
            margin = Integer.parseInt(ldString.substring(pipeIndex + 1));
            Preconditions.checkArgument(margin > 0, "Bad margin");
          }
        }
    
        String text;
        if (readBody) {
          text = CharStreams.toString(request.getReader());
        } else {
          text = request.getParameter("chl");
        }
        Preconditions.checkArgument(text != null && !text.isEmpty(), "No input");
    
        return new ChartServletRequestParameters(width, height, outputEncoding, ecLevel, margin, text);
      }
}
