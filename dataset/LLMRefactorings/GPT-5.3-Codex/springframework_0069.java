public class springframework_0069 {

    	@Override
    	public void close() throws IOException {
    		if (this.closed) {
    			applyExtractedRefactoring();

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

	private void applyExtractedRefactoring() {
    			return;
	}
}
