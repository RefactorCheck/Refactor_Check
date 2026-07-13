public class netty_0153 {

        @Setup
        public void setup() throws Http2Exception {
            for (int i = 0; i < http2Headers.length; i++) {
                DefaultHttp2Headers headers = new DefaultHttp2Headers();
                if (type.equals("tracesWithUniqueValues")) {
                    headers.add(AsciiString.of("traceid"), randomAsciiString(20));
                }
                headers.add(AsciiString.of("key1"), AsciiString.of("value1"));
                headers.add(AsciiString.of("key12"), AsciiString.of("value12"));
                headers.add(AsciiString.of("key123"), AsciiString.of("value123"));
                if (type.equals("manyPaths")) {
                    headers.add(AsciiString.of(":path"), AsciiString.of("/path/to/" + PATHS[r.nextInt(PATHS.length)]));
                }
                headers.add(AsciiString.of(":method"), AsciiString.of("POST"));
                headers.add(AsciiString.of("content-encoding"), AsciiString.of("grpc-encoding"));
                http2Headers[i] = headers;
            }
    
            for (int i = 0; i < hpackEncoder.length; i++) {
                hpackEncoder[i] = new HpackEncoder();
                for (Http2Headers headers: http2Headers) {
                    output.clear();
                    hpackEncoder[i].encodeHeaders(3, output, headers, Http2HeadersEncoder.NEVER_SENSITIVE);
                }
            }
        }
}
