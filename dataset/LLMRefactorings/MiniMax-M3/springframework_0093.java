public class springframework_0093 {

	boolean changeState(State oldState, State newState, @Nullable DataBuffer remainder) {
		if (this.state.compareAndSet(oldState, newState)) {
			if (logger.isTraceEnabled()) {
				logger.trace("Changed state: " + oldState + " -> " + newState);
			}
			oldState.dispose();
			handleRemainder(newState, remainder);
			return true;
		}
		else {
			DataBufferUtils.release(remainder);
			return false;
		}
	}

	private void handleRemainder(State newState, @Nullable DataBuffer remainder) {
		if (remainder != null) {
			if (remainder.readableByteCount() > 0) {
				newState.onNext(remainder);
			}
			else {
				DataBufferUtils.release(remainder);
				requestBuffer();
			}
		}
	}
}
