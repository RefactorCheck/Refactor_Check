public class netty_0153 {

    private static final String TYPE_TRACES_WITH_UNIQUE_VALUES = "tracesWithUniqueValues";
    private static final String TYPE_MANY_PATHS = "manyPaths";
    private static final AsciiString TRACE_ID = AsciiString.of("traceid");
    private static final AsciiString KEY1 = AsciiString.of("key1");
    private static final AsciiString KEY12 = AsciiString.of("key12");
    private static final AsciiString KEY123 = AsciiString.of("key123");
    private static final AsciiString VALUE1 = AsciiString.of("value1");
    private static final AsciiString VALUE12 = AsciiString.of("value12");
    private static final AsciiString VALUE123 = AsciiString.of("value123");
    private static final AsciiString PATH = AsciiString.of(":path");
    private static final AsciiString METHOD = AsciiString.of(":method");
    private static final AsciiString POST = AsciiString.of("POST");
    private static final AsciiString CONTENT_ENCODING = AsciiString.of("content-encoding");
    private static final AsciiString GRPC_ENCODING = AsciiString.of("grpc-encoding");

    @Setup
    public void setup() throws Http2Exception {
        for (int i = 0; i < http2Headers.length; i++) {
            DefaultHttp2Headers headers = new DefaultHttp2Headers();
            if (type.equals(TYPE_TRACES_WITH_UNIQUE_VALUES)) {
                headers.add(TRACE_ID, randomAsciiString(20));
            }
            headers.add(KEY1, VALUE1);
            headers.add(KEY12, VALUE12);
            headers.add(KEY123, VALUE123);
            if (type.equals(TYPE_MANY_PATHS)) {
                headers.add(PATH, AsciiString.of("/path/to/" + PATHS[r.nextInt(PATHS.length)]));
            }
            headers.add(METHOD, POST);
            headers.add(CONTENT_ENCODING, GRPC_ENCODING);
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
