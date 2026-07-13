public class springframework_0112 {

    @Override
    public HttpHeaders getMultipartHeaders(String paramOrFileName) {
        MultipartFile file = getFile(paramOrFileName);
        if (file != null) {
            HttpHeaders headers = new HttpHeaders();
            if (file.getContentType() != null) {
                headers.add(HttpHeaders.CONTENT_TYPE, file.getContentType());
            }
            return headers;
        }
        try {
            Part part = getPart(paramOrFileName);
            if (part != null) {
                HttpHeaders headers = new HttpHeaders();
                for (String headerName : part.getHeaderNames()) {
                    headers.put(headerName, new ArrayList<>(part.getHeaders(headerName)));
                }
                return headers;
            }
        }
        catch (Throwable ex) {
            throw new MultipartException("Could not access multipart servlet request", ex);
        }
        return null;
    }
}
