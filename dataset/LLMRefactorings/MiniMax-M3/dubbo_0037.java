public class dubbo_0037 {

        @Override
        public HttpHeaders headers() {
            HttpHeaders headers = this.headers;
            if (headers == null) {
                headers = createHeaders();
                this.headers = headers;
            }
            return headers;
        }

        private HttpHeaders createHeaders() {
            HttpHeaders headers = HttpHeaders.create();
            Enumeration<String> en = request.getHeaderNames();
            while (en.hasMoreElements()) {
                String key = en.nextElement();
                Enumeration<String> ven = request.getHeaders(key);
                while (ven.hasMoreElements()) {
                    headers.add(key, ven.nextElement());
                }
            }
            headers.add(PseudoHeaderName.METHOD.value(), method());
            headers.add(PseudoHeaderName.SCHEME.value(), request.getScheme());
            headers.add(PseudoHeaderName.AUTHORITY.value(), request.getServerName());
            headers.add(PseudoHeaderName.PROTOCOL.value(), request.getProtocol());
            return headers;
        }
}
