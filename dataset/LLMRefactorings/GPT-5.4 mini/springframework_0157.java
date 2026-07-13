public class springframework_0157 {

    	protected void resumePausedTasksRefactored() {
    		this.lifecycleLock.lock();
    		try {
    			if (!this.pausedTasks.isEmpty()) {
    				for (Iterator<?> it = this.pausedTasks.iterator(); it.hasNext();) {
    					Object task = it.next();
    					try {
    						doRescheduleTask(task);
    						it.remove();
    						if (logger.isDebugEnabled()) {
    							logger.debug("Resumed paused task: " + task);
    						}
    					}
    					catch (RuntimeException ex) {
    						logRejectedTask(task, ex);
    						// Keep the task in paused mode...
    					}
    				}
    			}
    		}
    		finally {
    			this.lifecycleLock.unlock();
    		}
    	}
}
