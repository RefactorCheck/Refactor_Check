public class zxing_0105 {

      @Override
      protected void doPost(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
        Collection<Part> parts;
        try {
          parts = request.getParts();
        } catch (Exception e) {
          // Includes IOException, InvalidContentTypeException, other parsing IllegalStateException
          log.info(e.toString());
          errorResponse(request, response, "badimage");
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
          errorResponse(request, response, "badimage");
        } else {
          log.info("Decoding uploaded file " + fileUploadPart.getSubmittedFileName());
          try (InputStream is = fileUploadPart.getInputStream()) {
            processStream(is, request, response);
          }
        }
      }
}
