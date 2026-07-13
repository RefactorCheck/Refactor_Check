public class springframework_0260 {

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
    					bos.write((byte)'\r');
    					bos.write((byte)'\n');
    				}
    			}
    			bos.write((byte)'\r');
    			bos.write((byte)'\n');
    
    			byte[] bytes = bos.toByteArrayUnsafe();
    			Mono<DataBuffer> extractedResult = bufferFactory.wrap(bytes);
    			return extractedResult;
    		});
    	}
}
