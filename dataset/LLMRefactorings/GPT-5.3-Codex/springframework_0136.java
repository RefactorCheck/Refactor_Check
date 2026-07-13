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

    			if ((getNextOrAwait()) == DONE) {
    				this.closed = true;
    				cleanAndFinalize();
    				if (this.error == null) {
    					return -1;
    				}
    				else {
    					throw Exceptions.propagate(this.error);
    				}
    			}
    			else if ((getNextOrAwait()) == CLOSED) {
    				cleanAndFinalize();
    				return -1;
    			}
    
    			return (getNextOrAwait()).read() & 0xFF;
    		}
    		catch (Throwable ex) {
    			this.closed = true;
    			requiredSubscriber().cancel();
    			cleanAndFinalize();
    			throw Exceptions.propagate(ex);
    		}
    		finally {
    			this.lock.unlock();
    		}
    	}
}
