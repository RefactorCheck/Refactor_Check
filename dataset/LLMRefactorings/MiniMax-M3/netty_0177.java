public class netty_0177 {

        @Setup(Level.Trial)
        public void setup() {
            Map<String, String> headers = ExampleHeaders.EXAMPLES.get(exampleHeader);
            httpNames = new AsciiString[headers.size()];
            http2Names = new AsciiString[headers.size()];
            httpValues = new AsciiString[headers.size()];
            httpWrongValues = new AsciiString[headers.size()];
            httpHeaders = new DefaultHttpHeaders(false);
            http2Headers = new DefaultHttp2Headers(false);
            int idx = 0;
            for (Map.Entry<String, String> header : headers.entrySet()) {
                addHeader(idx, header.getKey(), header.getValue());
                idx++;
            }
            slowHttp2Headers = new SlowHeaders(http2Headers);
            emptyHttpHeaders = new DefaultHttpHeaders(true);
            emptyHttp2Headers = new DefaultHttp2Headers(true);
            emptyHttpHeadersNoValidate = new DefaultHttpHeaders(false);
            emptyHttp2HeadersNoValidate = new DefaultHttp2Headers(false);
        }

        private void addHeader(int idx, String name, String value) {
            String httpName = toHttpName(name);
            String http2Name = toHttp2Name(name);
            httpNames[idx] = new AsciiString(httpName);
            http2Names[idx] = new AsciiString(http2Name);
            httpValues[idx] = new AsciiString(value);
            httpWrongValues[idx] = new AsciiString(value + "wrong");
            httpHeaders.add(httpNames[idx], httpValues[idx]);
            http2Headers.add(http2Names[idx], httpValues[idx]);
        }
}
