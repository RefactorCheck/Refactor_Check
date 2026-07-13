public class springframework_0260 {

    private static final byte[] CRLF = {(byte)'\r', (byte)'\n'};

    protected Mono<DataBuffer> generatePartHeaders(HttpHeaders headers, DataBufferFactory bufferFactory) {
        return Mono.fromCallable(() -> {
            @SuppressWarnings("resource")
            FastByteArrayOutputStream bos = new FastByteArrayOutputStream();
            for (Map.Entry<String, List<String>> entry : headers.headerSet()) {
                byte[] headerName = entry.getKey().getBytes(getCharset());
                for (String headerValueString : entry.getValue()) {
                    byte[] headerValue = headerValueString.getBytes(getCharset());
                    bos.write(headerName);
                    bos.write((byte)':');
                    bos.write((byte)' ');
                    bos.write(headerValue);
                    bos.write(CRLF);
                }
            }
            bos.write(CRLF);

            byte[] bytes = bos.toByteArrayUnsafe();
            return bufferFactory.wrap(bytes);
        });
    }
}
