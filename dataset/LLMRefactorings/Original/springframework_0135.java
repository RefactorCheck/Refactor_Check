public class springframework_0135 {

    	@Override
    	public Flux<DataBuffer> encode(Publisher<? extends ResourceRegion> input,
    			DataBufferFactory bufferFactory, ResolvableType elementType, @Nullable MimeType mimeType,
    			@Nullable Map<String, Object> hints) {
    
    		Assert.notNull(input, "'inputStream' must not be null");
    		Assert.notNull(bufferFactory, "'bufferFactory' must not be null");
    		Assert.notNull(elementType, "'elementType' must not be null");
    
    		if (input instanceof Mono) {
    			return Mono.from(input)
    					.flatMapMany(region -> {
    						if (!region.getResource().isReadable()) {
    							return Flux.error(new EncodingException(
    									"Resource " + region.getResource() + " is not readable"));
    						}
    						return writeResourceRegion(region, bufferFactory, hints);
    					});
    		}
    		else {
    			final String boundaryString = Hints.getRequiredHint(hints, BOUNDARY_STRING_HINT);
    			byte[] startBoundary = toAsciiBytes("\r\n--" + boundaryString + "\r\n");
    			byte[] contentType = mimeType != null ? toAsciiBytes("Content-Type: " + mimeType + "\r\n") : new byte[0];
    
    			return Flux.from(input)
    					.concatMap(region -> {
    						if (!region.getResource().isReadable()) {
    							return Flux.error(new EncodingException(
    									"Resource " + region.getResource() + " is not readable"));
    						}
    						Flux<DataBuffer> prefix = Flux.just(
    								bufferFactory.wrap(startBoundary),
    								bufferFactory.wrap(contentType),
    								bufferFactory.wrap(getContentRangeHeader(region))); // only wrapping, no allocation
    
    						return prefix.concatWith(writeResourceRegion(region, bufferFactory, hints));
    					})
    					.concatWithValues(getRegionSuffix(bufferFactory, boundaryString));
    		}
    		// No doOnDiscard (no caching after DataBufferUtils#read)
    	}
}
