public class springframework_0008 {
    private byte[][] delimiterBytes;


    	@Override
    	public final Flux<T> decode(Publisher<DataBuffer> input, ResolvableType elementType,
    			@Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
    
    		this.delimiterBytes = getDelimiterBytes(mimeType);
    
    		LimitedDataBufferList chunks = new LimitedDataBufferList(getMaxInMemorySize());
    		DataBufferUtils.Matcher matcher = DataBufferUtils.matcher(delimiterBytes);
    
    		return Flux.from(input)
    				.concatMapIterable(buffer -> processDataBuffer(buffer, matcher, chunks))
    				.concatWith(Mono.defer(() -> {
    					if (chunks.isEmpty()) {
    						return Mono.empty();
    					}
    					DataBuffer lastBuffer = chunks.get(0).factory().join(chunks);
    					chunks.clear();
    					return Mono.just(lastBuffer);
    				}))
    				.doFinally(signalType -> chunks.releaseAndClear())
    				.doOnDiscard(DataBuffer.class, DataBufferUtils::release)
    				.map(buffer -> decode(buffer, elementType, mimeType, hints));
    	}
}
