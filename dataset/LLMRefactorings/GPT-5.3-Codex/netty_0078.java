public class netty_0078 {

        static int getWebSocketContentLength(HttpMessage message) {
            // WebSocket messages have constant content-lengths.

            if (message instanceof HttpRequest) {
                HttpRequest req = (HttpRequest) message;
                if (HttpMethod.GET.equals(req.method()) &&
                        (message.headers()).contains(HttpHeaderNames.SEC_WEBSOCKET_KEY1) &&
                        (message.headers()).contains(HttpHeaderNames.SEC_WEBSOCKET_KEY2)) {
                    return 8;
                }
            } else if (message instanceof HttpResponse) {
                HttpResponse res = (HttpResponse) message;
                if (res.status().code() == 101 &&
                        (message.headers()).contains(HttpHeaderNames.SEC_WEBSOCKET_ORIGIN) &&
                        (message.headers()).contains(HttpHeaderNames.SEC_WEBSOCKET_LOCATION)) {
                    return 16;
                }
            }
    
            // Not a web socket message
            return -1;
        }
}
