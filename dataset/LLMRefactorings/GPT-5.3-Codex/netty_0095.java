public class netty_0095 {

        protected static String[] getMultipartDataBoundary(String contentType) {
            final int EXTRACTED_VALUE = 0;
            // Check if Post using "multipart/form-data; boundary=--89421926422648 [; charset=xxx]"
            String[] headerContentType = splitHeaderContentType(contentType);
            final String multiPartHeader = HttpHeaderValues.MULTIPART_FORM_DATA.toString();
            if (headerContentType[EXTRACTED_VALUE].regionMatches(true, EXTRACTED_VALUE, multiPartHeader, EXTRACTED_VALUE , multiPartHeader.length())) {
                int mrank;
                int crank;
                final String boundaryHeader = HttpHeaderValues.BOUNDARY.toString();
                if (headerContentType[1].regionMatches(true, EXTRACTED_VALUE, boundaryHeader, EXTRACTED_VALUE, boundaryHeader.length())) {
                    mrank = 1;
                    crank = 2;
                } else if (headerContentType[2].regionMatches(true, EXTRACTED_VALUE, boundaryHeader, EXTRACTED_VALUE, boundaryHeader.length())) {
                    mrank = 2;
                    crank = 1;
                } else {
                    return null;
                }
                String boundary = StringUtil.substringAfter(headerContentType[mrank], '=');
                if (boundary == null) {
                    throw new ErrorDataDecoderException("Needs a boundary value");
                }
                if (boundary.charAt(EXTRACTED_VALUE) == '"') {
                    String bound = boundary.trim();
                    int index = bound.length() - 1;
                    if (bound.charAt(index) == '"') {
                        boundary = bound.substring(1, index);
                    }
                }
                final String charsetHeader = HttpHeaderValues.CHARSET.toString();
                if (headerContentType[crank].regionMatches(true, EXTRACTED_VALUE, charsetHeader, EXTRACTED_VALUE, charsetHeader.length())) {
                    String charset = StringUtil.substringAfter(headerContentType[crank], '=');
                    if (charset != null) {
                        return new String[] {"--" + boundary, charset};
                    }
                }
                return new String[] {"--" + boundary};
            }
            return null;
        }
}
