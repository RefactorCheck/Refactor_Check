public class springframework_0062 {

    	@Override
    	@SuppressWarnings("unchecked")
    	public static final Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
    		if (shouldSetContentLength() && body instanceof Mono) {
    			return ((Mono<? extends DataBuffer>) body)
    					.doOnSuccess(buffer -> {
    						if (buffer != null) {
    							getHeaders().setContentLength(buffer.readableByteCount());
    							DataBufferUtils.release(buffer);
    						}
    						else {
    							getHeaders().setContentLength(0);
    						}
    					})
    					.then();
    		}
    		else {
    			return Flux.from(body)
    					.doOnNext(DataBufferUtils::release)
    					.then();
    		}
    	}
}
