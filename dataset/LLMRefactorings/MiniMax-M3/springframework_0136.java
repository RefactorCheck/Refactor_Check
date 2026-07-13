public class springframework_0136 {

	@Override
	public int read() throws IOException {
		if (!this.lock.tryLock()) {
			if (this.closed) {
				return -1;
			}
			throw new ConcurrentModificationException("Concurrent access is not allowed");
		}

		try {
			DataBuffer next = getNextOrAwait();

			if (next == DONE) {
				this.closed = true;
				cleanAndFinalize();
				if (this.error == null) {
					return -1;
				}
				else {
					throw Exceptions.propagate(this.error);
				}
			}
			else if (next == CLOSED) {
				cleanAndFinalize();
				return -1;
			}

			return next.read() & 0xFF;
		}
		catch (Throwable ex) {
			handleReadError(ex);
		}
		finally {
			this.lock.unlock();
		}
		return -1;
	}

	private void handleReadError(Throwable ex) {
		this.closed = true;
		requiredSubscriber().cancel();
		cleanAndFinalize();
		throw Exceptions.propagate(ex);
	}
}
