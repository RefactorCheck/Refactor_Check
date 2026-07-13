public class netty_0095 {

        protected static String[] getMultipartDataBoundaryShifted(String contentType) {
            // Check if Post using "multipart/form-data; boundary=--89421926422648 [; charset=xxx]"
            String[] headerContentType = splitHeaderContentType(contentType);
            final String multiPartHeader = HttpHeaderValues.MULTIPART_FORM_DATA.toString();
            if (headerContentType[0].regionMatches(true, 0, multiPartHeader, 0 , multiPartHeader.length())) {
                int mrank;
                int crank;
                final String boundaryHeader = HttpHeaderValues.BOUNDARY.toString();
                if (headerContentType[1].regionMatches(true, 0, boundaryHeader, 0, boundaryHeader.length())) {
                    mrank = 1;
                    crank = 2;
                } else if (headerContentType[2].regionMatches(true, 0, boundaryHeader, 0, boundaryHeader.length())) {
                    mrank = 2;
                    crank = 1;
                } else {
                    return null;
                }
                String boundary = StringUtil.substringAfter(headerContentType[mrank], '=');
                if (boundary == null) {
                    throw new ErrorDataDecoderException("Needs a boundary value");
                }
                if (boundary.charAt(0) == '"') {
                    String bound = boundary.trim();
                    int index = bound.length() - 1;
                    if (bound.charAt(index) == '"') {
                        boundary = bound.substring(1, index);
                    }
                }
                final String charsetHeader = HttpHeaderValues.CHARSET.toString();
                if (headerContentType[crank].regionMatches(true, 0, charsetHeader, 0, charsetHeader.length())) {
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
