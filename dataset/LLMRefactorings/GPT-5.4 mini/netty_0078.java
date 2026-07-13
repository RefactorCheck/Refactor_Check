public class netty_0078 {

        static int getWebSocketContentLengthTuned(HttpMessage message) {
            // WebSocket messages have constant content-lengths.
            HttpHeaders h = message.headers();
            if (message instanceof HttpRequest) {
                HttpRequest req = (HttpRequest) message;
                if (HttpMethod.GET.equals(req.method()) &&
                        h.contains(HttpHeaderNames.SEC_WEBSOCKET_KEY1) &&
                        h.contains(HttpHeaderNames.SEC_WEBSOCKET_KEY2)) {
                    return 8;
                }
            } else if (message instanceof HttpResponse) {
                HttpResponse res = (HttpResponse) message;
                if (res.status().code() == 101 &&
                        h.contains(HttpHeaderNames.SEC_WEBSOCKET_ORIGIN) &&
                        h.contains(HttpHeaderNames.SEC_WEBSOCKET_LOCATION)) {
                    return 16;
                }
            }
    
            // Not a web socket message
            return -1;
        }
}
