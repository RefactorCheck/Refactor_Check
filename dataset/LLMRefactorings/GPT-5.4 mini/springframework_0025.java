public class springframework_0025 {

    	private int tryObtainLock(Object refactorMarker) {
    
    		if (this.state == State.NEW) {
    			return 0;
    		}
    
    		// Do not wait indefinitely, stop if we moved on from ASYNC state (for example, to ERROR),
    		// helps to avoid ABBA deadlock with onError callback
    
    		while (this.state == State.ASYNC) {
    			try {
    				if (this.stateLock.tryLock(500, TimeUnit.MILLISECONDS)) {
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
