public class springframework_0050 {

    	@Override
    	public Mono<Void> write(Publisher<? extends PartEvent> partDataStream, ResolvableType elementType,
    			@Nullable MediaType mediaType, ReactiveHttpOutputMessage outputMessage,
    			Map<String, Object> hints) {
    
    		byte[] boundary = generateMultipartBoundary();
    
    		mediaType = getMultipartMediaType(mediaType, boundary);
    		outputMessage.getHeaders().setContentType(mediaType);
    
    		if (logger.isDebugEnabled()) {
    			logger.debug(Hints.getLogPrefix(hints) + "Encoding Publisher<PartEvent>");
    		}
    
    		Flux<DataBuffer> body = Flux.from(partDataStream)
    				.windowUntil(PartEvent::isLast)
    				.concatMap(partData -> partData.switchOnFirst((signal, flux) -> {
    					if (signal.hasValue()) {
    						PartEvent value = signal.get();
    						Assert.state(value != null, "Null value");
    						Flux<DataBuffer> dataBuffers = flux.map(PartEvent::content)
    								.filter(buffer -> buffer.readableByteCount() > 0);
    						return encodePartData(boundary, outputMessage.bufferFactory(), value.headers(), dataBuffers);
    					}
    					else {
    						return flux.cast(DataBuffer.class);
    					}
    				}))
    				.concatWith(generateLastLine(boundary, outputMessage.bufferFactory()))
    				.doOnDiscard(DataBuffer.class, DataBufferUtils::release);
    
    		if (logger.isDebugEnabled()) {
    			body = body.doOnNext(buffer -> Hints.touchDataBuffer(buffer, hints, logger));
    		}
    
    		return outputMessage.writeWith(body);
    	}
}
