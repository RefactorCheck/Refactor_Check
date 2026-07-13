public class springframework_0157 {

	protected void resumePausedTasks() {
		this.lifecycleLock.lock();
		try {
			if (!this.pausedTasks.isEmpty()) {
				for (Iterator<?> it = this.pausedTasks.iterator(); it.hasNext();) {
					Object task = it.next();
					processPausedTask(it, task);
				}
			}
		}
		finally {
			this.lifecycleLock.unlock();
		}
	}

	private void processPausedTask(Iterator<?> it, Object task) {
		try {
			doRescheduleTask(task);
			it.remove();
			if (logger.isDebugEnabled()) {
				logger.debug("Resumed paused task: " + task);
			}
		}
		catch (RuntimeException ex) {
			logRejectedTask(task, ex);
		}
	}
}
