public class springframework_0118 {

    static void addHeaders(HttpURLConnection connection, HttpHeaders headers) {
        String method = connection.getRequestMethod();
        if (method.equals("PUT") || method.equals("DELETE")) {
            if (!StringUtils.hasText(headers.getFirst(HttpHeaders.ACCEPT))) {
                // Avoid "text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2"
                // from HttpUrlConnection which prevents JSON error response details.
                headers.set(HttpHeaders.ACCEPT, "*/*");
            }
        }
        headers.forEach((headerName, headerValues) -> {
            if (HttpHeaders.COOKIE.equalsIgnoreCase(headerName)) {  // RFC 6265
                addCookieHeader(connection, headerName, headerValues);
            }
            else {
                addRequestHeaders(connection, headerName, headerValues);
            }
        });
    }

    private static void addCookieHeader(HttpURLConnection connection, String headerName, java.util.List<String> headerValues) {
        String headerValue = StringUtils.collectionToDelimitedString(headerValues, "; ");
        connection.setRequestProperty(headerName, headerValue);
    }

    private static void addRequestHeaders(HttpURLConnection connection, String headerName, java.util.List<String> headerValues) {
        for (String headerValue : headerValues) {
            String actualHeaderValue = headerValue != null ? headerValue : "";
            connection.addRequestProperty(headerName, actualHeaderValue);
        }
    }
}
