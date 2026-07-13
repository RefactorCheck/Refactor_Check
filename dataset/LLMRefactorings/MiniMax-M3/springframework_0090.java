public class springframework_0090 {

	private void checkSessionLimits() {
		if (!shouldNotSend()) {
			if (getTimeSinceSendStarted() > getSendTimeLimit()) {
				raiseLimitExceeded("Send time %d (ms) for session '%s' exceeded the allowed limit %d",
						getTimeSinceSendStarted(), getId(), getSendTimeLimit());
			}
			else if (getBufferSize() > getBufferSizeLimit()) {
				switch (this.overflowStrategy) {
					case TERMINATE -> {
						raiseLimitExceeded("Buffer size %d bytes for session '%s' exceeds the allowed limit %d",
								getBufferSize(), getId(), getBufferSizeLimit());
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

	private void raiseLimitExceeded(String format, Object... args) {
		String reason = String.format(format, args);
		limitExceeded(reason);
	}
}
