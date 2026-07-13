public class springframework_0260 {

    	protected Mono<DataBuffer> generatePartHeaders(HttpHeaders headers, DataBufferFactory bufferFactory) {
    		return Mono.fromCallable(() -> {
    			@SuppressWarnings("resource")
    			for (Map.Entry<String, List<String>> entry : headers.headerSet()) {
    				byte[] headerName = entry.getKey().getBytes(getCharset());
    				for (String headerValueString : entry.getValue()) {
    					byte[] headerValue = headerValueString.getBytes(getCharset());
    					(new FastByteArrayOutputStream()).write(headerName);
    					(new FastByteArrayOutputStream()).write((byte)':');
    					(new FastByteArrayOutputStream()).write((byte)' ');
    					(new FastByteArrayOutputStream()).write(headerValue);
    					(new FastByteArrayOutputStream()).write((byte)'\r');
    					(new FastByteArrayOutputStream()).write((byte)'\n');
    				}
    			}
    			(new FastByteArrayOutputStream()).write((byte)'\r');
    			(new FastByteArrayOutputStream()).write((byte)'\n');
    
    			byte[] bytes = (new FastByteArrayOutputStream()).toByteArrayUnsafe();
    			return bufferFactory.wrap(bytes);
    		});
    	}
}
