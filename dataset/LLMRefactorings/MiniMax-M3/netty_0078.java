public class netty_0078 {

    private static final int WEBSOCKET_REQUEST_CONTENT_LENGTH = 8;
    private static final int WEBSOCKET_RESPONSE_CONTENT_LENGTH = 16;
    private static final int NOT_WEBSOCKET = -1;

    static int getWebSocketContentLength(HttpMessage message) {
        HttpHeaders h = message.headers();
        if (message instanceof HttpRequest) {
            HttpRequest req = (HttpRequest) message;
            if (HttpMethod.GET.equals(req.method()) &&
                    h.contains(HttpHeaderNames.SEC_WEBSOCKET_KEY1) &&
                    h.contains(HttpHeaderNames.SEC_WEBSOCKET_KEY2)) {
                return WEBSOCKET_REQUEST_CONTENT_LENGTH;
            }
        } else if (message instanceof HttpResponse) {
            HttpResponse res = (HttpResponse) message;
            if (res.status().code() == 101 &&
                    h.contains(HttpHeaderNames.SEC_WEBSOCKET_ORIGIN) &&
                    h.contains(HttpHeaderNames.SEC_WEBSOCKET_LOCATION)) {
                return WEBSOCKET_RESPONSE_CONTENT_LENGTH;
            }
        }

        return NOT_WEBSOCKET;
    }
}
