public class springframework_0222 {

	private void readHeaders(ByteBuffer byteBuffer, StompHeaderAccessor headerAccessor, StompCommand command) {

		boolean shouldUnescape = (command != StompCommand.CONNECT &&
				command != StompCommand.CONNECTED &&
				command != StompCommand.STOMP);

		while (true) {
			ByteArrayOutputStream headerStream = new ByteArrayOutputStream(256);
			boolean headerComplete = false;
			while (byteBuffer.hasRemaining()) {
				if (tryConsumeEndOfLine(byteBuffer)) {
					headerComplete = true;
					break;
				}
				headerStream.write(byteBuffer.get());
			}
			if (headerStream.size() > 0 && headerComplete) {
				String header = StreamUtils.copyToString(headerStream, StandardCharsets.UTF_8);
				int colonIndex = header.indexOf(':');
				if (colonIndex <= 0) {
					if (byteBuffer.remaining() > 0) {
						throw new StompConversionException("Illegal header: '" + header +
								"'. A header must be of the form <name>:[<value>].");
					}
				}
				else {
					String rawHeaderName = header.substring(0, colonIndex);
					String rawHeaderValue = header.substring(colonIndex + 1);
					String headerName = shouldUnescape ? unescape(rawHeaderName) : rawHeaderName;
					String headerValue = shouldUnescape ? unescape(rawHeaderValue) : rawHeaderValue;
					try {
						headerAccessor.addNativeHeader(headerName, headerValue);
					}
					catch (InvalidMimeTypeException ex) {
						if (byteBuffer.remaining() > 0) {
							throw ex;
						}
					}
				}
			}
			else {
				break;
			}
		}
	}
}
