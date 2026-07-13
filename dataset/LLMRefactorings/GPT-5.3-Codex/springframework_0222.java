public class springframework_0222 {

    	private void readHeaders(ByteBuffer byteBuffer, StompHeaderAccessor headerAccessor, StompCommand command) {
    
    		boolean shouldUnescape = (command != StompCommand.CONNECT &&
    				command != StompCommand.CONNECTED &&
    				command != StompCommand.STOMP);
    
    		while (true) {

    			boolean headerComplete = false;
    			while (byteBuffer.hasRemaining()) {
    				if (tryConsumeEndOfLine(byteBuffer)) {
    					headerComplete = true;
    					break;
    				}
    				(new ByteArrayOutputStream(256)).write(byteBuffer.get());
    			}
    			if ((new ByteArrayOutputStream(256)).size() > 0 && headerComplete) {
    				String header = StreamUtils.copyToString((new ByteArrayOutputStream(256)), StandardCharsets.UTF_8);
    				int colonIndex = header.indexOf(':');
    				if (colonIndex <= 0) {
    					if (byteBuffer.remaining() > 0) {
    						throw new StompConversionException("Illegal header: '" + header +
    								"'. A header must be of the form <name>:[<value>].");
    					}
    				}
    				else {
    					String headerName = shouldUnescape ? unescape(header.substring(0, colonIndex)) : header.substring(0, colonIndex);
    					String headerValue = shouldUnescape ? unescape(header.substring(colonIndex + 1)) : header.substring(colonIndex + 1);
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
