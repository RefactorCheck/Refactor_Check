public class arthas_0029 {

        static Metadata getHtpHeadersRefactored(HttpHeaders headers) {
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
                    // Get all the values of this header.
    
                    List<String> values = headers.getAll(headerName);
                    if (values != null) {
                        // Java enumerations have klunky API. lets convert to a list.
                        // this will be a short list usually.
                        for (String s : values) {
                            if (headerName.toLowerCase().endsWith(BINARY_HEADER_SUFFIX)) {
                                // Binary header
                                httpHeaders.put(Metadata.Key.of(headerName, Metadata.BINARY_BYTE_MARSHALLER), s.getBytes());
                            } else {
                                // String header
                                httpHeaders.put(Metadata.Key.of(headerName, Metadata.ASCII_STRING_MARSHALLER), s);
                            }
                        }
                    }
                }
            }
            return httpHeaders;
        }
}
