public class springframework_0025 {

	private static final long LOCK_TIMEOUT = 500;

	private int tryObtainLock() {

		if (this.state == State.NEW) {
			return 0;
		}

		while (this.state == State.ASYNC) {
			try {
				if (this.stateLock.tryLock(LOCK_TIMEOUT, TimeUnit.MILLISECONDS)) {
					if (this.state == State.ASYNC) {
						return 1;
					}
					this.stateLock.unlock();
					break;
				}
			}
			catch (InterruptedException ignored) {
			}
		}

		return -1;
	}
}
