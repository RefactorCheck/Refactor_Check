public class springframework_0069 {

    	@Override
    	public void close() throws IOException {
    		if (this.closed) {
    			return;
    		}
    
    		this.closed = true;
    
    		if (!this.lock.tryLock()) {
    			if (addWork() == 0) {
    				resume();
    			}
    			return;
    		}
    
    		try {
    			requiredSubscriber().cancel();
    			cleanAndFinalize();
    		}
    		finally {
    			this.lock.unlock();
    		}
    	}
}
