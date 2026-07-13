public class zxing_0100 {

      private static void processStream(InputStream is,
                                        HttpServletRequest request,
                                        HttpServletResponse response) throws ServletException, IOException {
    
        BufferedImage image;
        try {
          image = ImageIO.read(is);
        } catch (Exception e) {
          // Many possible failures from JAI, so just catch anything as a failure
          log.info(e.toString());
          errorResponse(request, response, "badimage");
          return;
        }
        if (image == null) {
          errorResponse(request, response, "badimage");
          return;
        }
        try {
          int height = image.getHeight();
          int width = image.getWidth();
          if (height <= 1 || width <= 1) {
            log.info("Dimensions too small: " + width + 'x' + height);
            errorResponse(request, response, "badimage");
            return;
          } else if (height * width > MAX_PIXELS) {
            log.info("Dimensions too large: " + width + 'x' + height);
            errorResponse(request, response, HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE, "badimage");
            return;
          }
    
          processImage(image, request, response);
        } finally {
          image.flush();
        }
      }
}
