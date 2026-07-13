public class arthas_0029 {

    static Metadata getHtpHeaders(HttpHeaders headers) {
        Metadata httpHeaders = new Metadata();

        Set<String> headerNames = headers.names();
        if (headerNames == null) {
            return httpHeaders;
        }
        // copy all headers "x-grpc-*" into Metadata
        // TODO: do we need to copy all "x-*" headers instead?
        for (String headerName : headerNames) {
            if (EXCLUDED.contains(headerName.toLowerCase())) {
                continue;
            }
            if (headerName.toLowerCase().startsWith(GRPC_HEADER_PREFIX)) {
                List<String> values = headers.getAll(headerName);
                if (values != null) {
                    for (String value : values) {
                        putHeader(httpHeaders, headerName, value);
                    }
                }
            }
        }
        return httpHeaders;
    }

    private static void putHeader(Metadata metadata, String headerName, String value) {
        if (headerName.toLowerCase().endsWith(BINARY_HEADER_SUFFIX)) {
            metadata.put(Metadata.Key.of(headerName, Metadata.BINARY_BYTE_MARSHALLER), value.getBytes());
        } else {
            metadata.put(Metadata.Key.of(headerName, Metadata.ASCII_STRING_MARSHALLER), value);
        }
    }
}
