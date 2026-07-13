public class springframework_0255 {

    	@Override
    	public Flux<PartEvent> read(ResolvableType elementType, ReactiveHttpInputMessage message,
    			Map<String, Object> hints) {
    
    		return Flux.defer(() -> {
    			byte[] boundary = MultipartUtils.boundary(message, this.headersCharset);
    			if (boundary == null) {
    				return Flux.error(new DecodingException("No multipart boundary found in Content-Type: \"" +
    						message.getHeaders().getContentType() + "\""));
    			}
    			Flux<MultipartParser.Token> allPartsTokens = MultipartParser.parse(message.getBody(), boundary,
    					this.maxHeadersSize, this.headersCharset);
    
    			AtomicInteger partCount = new AtomicInteger();
    			return allPartsTokens
    					.windowUntil(HeadersToken.class::isInstance, true)
    					.concatMap(partTokens -> partTokens
    							.switchOnFirst((signal, flux) -> {
    								if (!signal.hasValue()) {
    									// complete or error signal
    									return flux.cast(PartEvent.class);
    								}
    								else if (tooManyParts(partCount)) {
    									return Mono.error(new DecodingException("Too many parts (" + partCount.get() +
    											"/" + this.maxParts + " allowed)"));
    								}
    								MultipartParser.HeadersToken headersToken = (MultipartParser.HeadersToken) signal.get();
    								Assert.state(headersToken != null, "Signal should be headers token");
    
    								HttpHeaders headers = headersToken.headers();
    								return createEvents(headers, flux.ofType(MultipartParser.BodyToken.class));
    							}));
    		});
    	}
}
