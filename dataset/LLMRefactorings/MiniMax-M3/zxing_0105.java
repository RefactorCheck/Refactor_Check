public class zxing_0105 {

  private static final String ERROR_BAD_IMAGE = "badimage";

      @Override
      protected void doPost(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
        Collection<Part> parts;
        try {
          parts = request.getParts();
        } catch (Exception e) {
          log.info(e.toString());
          errorResponse(request, response, ERROR_BAD_IMAGE);
          return;
        }
        Part fileUploadPart = null;
        for (Part part : parts) {
          if (part.getHeader(HttpHeaders.CONTENT_DISPOSITION) != null) {
            fileUploadPart = part;
            break;
          }
        }
        if (fileUploadPart == null) {
          log.info("File upload was not multipart");
          errorResponse(request, response, ERROR_BAD_IMAGE);
        } else {
          log.info("Decoding uploaded file " + fileUploadPart.getSubmittedFileName());
          try (InputStream is = fileUploadPart.getInputStream()) {
            processStream(is, request, response);
          }
        }
      }
}
