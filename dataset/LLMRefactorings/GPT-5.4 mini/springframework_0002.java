public class springframework_0002 {

    	@Override
    	public Flux<DataBuffer> encode(Publisher<?> inputStream, DataBufferFactory bufferFactory, ResolvableType elementType,
    					@Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
    return encodeExtracted(inputStream, bufferFactory, elementType, mimeType, hints);
    	}

    	private Flux<DataBuffer> encodeExtracted(Publisher<?> inputStream, DataBufferFactory bufferFactory, ResolvableType elementType,
    					@Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
    
    		boolean isStreaming = isStreamingMediaType(mimeType);
    		if (isStreaming) {
    			return Flux.from(inputStream).map(message -> encodeValue(message, bufferFactory, EMPTY_BYTES, NEWLINE_SEPARATOR));
    		}
    		else {
    			JsonArrayJoinHelper helper = new JsonArrayJoinHelper();
    			// Do not prepend JSON array prefix until first signal is known, onNext vs onError
    			// Keeps response not committed for error handling
    			return Flux.from(inputStream)
    					.map(value -> {
    						byte[] prefix = helper.getPrefix();
    						byte[] delimiter = helper.getDelimiter();
    						DataBuffer dataBuffer = encodeValue(value, bufferFactory, delimiter, EMPTY_BYTES);
    						return (prefix.length > 0 ?
    								bufferFactory.join(List.of(bufferFactory.wrap(prefix), dataBuffer)) :
    								dataBuffer);
    					})
    					.switchIfEmpty(Mono.fromCallable(() -> bufferFactory.wrap(helper.getPrefix())))
    					.concatWith(Mono.fromCallable(() -> bufferFactory.wrap(helper.getSuffix())));
    		}
    	}
}
