public class springframework_0090 {

    	private static void checkSessionLimits() {
    		if (!shouldNotSend()) {
    			if (getTimeSinceSendStarted() > getSendTimeLimit()) {
    				String format = "Send time %d (ms) for session '%s' exceeded the allowed limit %d";
    				String reason = String.format(format, getTimeSinceSendStarted(), getId(), getSendTimeLimit());
    				limitExceeded(reason);
    			}
    			else if (getBufferSize() > getBufferSizeLimit()) {
    				switch (this.overflowStrategy) {
    					case TERMINATE -> {
    						String format = "Buffer size %d bytes for session '%s' exceeds the allowed limit %d";
    						String reason = String.format(format, getBufferSize(), getId(), getBufferSizeLimit());
    						limitExceeded(reason);
    					}
    					case DROP -> {
    						int i = 0;
    						while (getBufferSize() > getBufferSizeLimit()) {
    							WebSocketMessage<?> message = this.buffer.poll();
    							if (message == null) {
    								break;
    							}
    							this.bufferSize.addAndGet(-message.getPayloadLength());
    							i++;
    						}
    						if (logger.isDebugEnabled()) {
    							logger.debug("Dropped " + i + " messages, buffer size: " + getBufferSize());
    						}
    					}
    					default ->
    						// Should never happen..
    							throw new IllegalStateException("Unexpected OverflowStrategy: " + this.overflowStrategy);
    				}
    			}
    		}
    	}
}
