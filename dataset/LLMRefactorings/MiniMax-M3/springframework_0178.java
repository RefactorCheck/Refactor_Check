public class springframework_0178 {

    	private Mono<Payload> getSetupPayload(
    			MimeType dataMimeType, MimeType metaMimeType, RSocketStrategies strategies) {
    
    		Object data = this.setupData;
    		boolean hasMetadata = (this.setupRoute != null || !CollectionUtils.isEmpty(this.setupMetadata));
    		if (!hasMetadata && data == null) {
    			return Mono.just(EMPTY_SETUP_PAYLOAD);
    		}
    
    		Mono<DataBuffer> dataMono = (data != null)
    				? encodeSetupData(data, dataMimeType, strategies)
    				: Mono.empty();
    
    		Mono<DataBuffer> metaMono = Mono.empty();
    		if (hasMetadata) {
    			metaMono = new MetadataEncoder(metaMimeType, strategies)
    					.metadataAndOrRoute(this.setupMetadata, this.setupRoute, this.setupRouteVars)
    					.encode();
    		}
    
    		Mono<DataBuffer> emptyBuffer = Mono.fromCallable(() ->
    				strategies.dataBufferFactory().wrap(EMPTY_BYTE_ARRAY));
    
    		dataMono = dataMono.switchIfEmpty(emptyBuffer);
    		metaMono = metaMono.switchIfEmpty(emptyBuffer);
    
    		return Mono.zip(dataMono, metaMono)
    				.map(tuple -> PayloadUtils.createPayload(tuple.getT1(), tuple.getT2()))
    				.doOnDiscard(DataBuffer.class, DataBufferUtils::release)
    				.doOnDiscard(Payload.class, Payload::release);
    	}

    	private Mono<DataBuffer> encodeSetupData(Object data, MimeType dataMimeType, RSocketStrategies strategies) {
    		ReactiveAdapter adapter = strategies.reactiveAdapterRegistry().getAdapter(data.getClass());
    		Assert.isTrue(adapter == null || !adapter.isMultiValue(), () -> "Expected single value: " + data);
    		Mono<?> mono = (adapter != null ? Mono.from(adapter.toPublisher(data)) : Mono.just(data));
    		return mono.map(value -> {
    			ResolvableType type = ResolvableType.forClass(value.getClass());
    			Encoder<Object> encoder = strategies.encoder(type, dataMimeType);
    			Assert.notNull(encoder, () -> "No encoder for " + dataMimeType + ", " + type);
    			return encoder.encodeValue(value, strategies.dataBufferFactory(), type, dataMimeType, HINTS);
    		});
    	}
}
