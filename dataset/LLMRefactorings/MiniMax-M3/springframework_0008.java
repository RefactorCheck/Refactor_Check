public class springframework_0008 {

	@Override
	public final Flux<T> decode(Publisher<DataBuffer> input, ResolvableType elementType,
			@Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {

		byte[][] delimiterBytes = getDelimiterBytes(mimeType);

		LimitedDataBufferList chunks = new LimitedDataBufferList(getMaxInMemorySize());
		DataBufferUtils.Matcher matcher = DataBufferUtils.matcher(delimiterBytes);

		return Flux.from(input)
				.concatMapIterable(buffer -> processDataBuffer(buffer, matcher, chunks))
				.concatWith(Mono.defer(() -> emitLastBuffer(chunks)))
				.doFinally(signalType -> chunks.releaseAndClear())
				.doOnDiscard(DataBuffer.class, DataBufferUtils::release)
				.map(buffer -> decode(buffer, elementType, mimeType, hints));
	}

	private Mono<DataBuffer> emitLastBuffer(LimitedDataBufferList chunks) {
		if (chunks.isEmpty()) {
			return Mono.empty();
		}
		DataBuffer lastBuffer = chunks.get(0).factory().join(chunks);
		chunks.clear();
		return Mono.just(lastBuffer);
	}
}
