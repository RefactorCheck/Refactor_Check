public class springframework_0230 {

	@Override
	@SuppressWarnings("unchecked")
	public final Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
		if (body instanceof Mono) {
			return writeWithMono((Mono<? extends DataBuffer>) body);
		}
		else {
			return new ChannelSendOperator<>(body, inner -> doCommit(() -> writeWithInternal(inner)))
					.doOnError(t -> getHeaders().clearContentHeaders());
		}
	}

	private Mono<Void> writeWithMono(Mono<? extends DataBuffer> body) {
		return body
				.flatMap(buffer -> {
					touchDataBuffer(buffer);
					AtomicBoolean subscribed = new AtomicBoolean();
					return doCommit(
							() -> {
								try {
									return writeWithInternal(Mono.fromCallable(() -> buffer)
											.doOnSubscribe(s -> subscribed.set(true))
											.doOnDiscard(DataBuffer.class, DataBufferUtils::release));
								}
								catch (Throwable ex) {
									return Mono.error(ex);
								}
							})
							.doOnError(ex -> DataBufferUtils.release(buffer))
							.doOnCancel(() -> {
								if (!subscribed.get()) {
									DataBufferUtils.release(buffer);
								}
							});
				})
				.doOnError(t -> getHeaders().clearContentHeaders())
				.doOnDiscard(DataBuffer.class, DataBufferUtils::release);
	}
}
