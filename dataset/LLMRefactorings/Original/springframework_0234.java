public class springframework_0234 {

    	private void await() {
    		Thread toUnpark = Thread.currentThread();
    
    		while (true) {
    			Object current = this.parkedThread.get();
    			if (current == READY) {
    				break;
    			}
    
    			if (current != null && current != toUnpark) {
    				throw new IllegalStateException("Only one (Virtual)Thread can await!");
    			}
    
    			if (this.parkedThread.compareAndSet( null, toUnpark)) {
    				LockSupport.park();
    				// we don't just break here because park() can wake up spuriously
    				// if we got a proper resume, get() == READY and the loop will quit above
    			}
    		}
    		// clear the resume indicator so that the next await call will park without a resume()
    		this.parkedThread.lazySet(null);
    	}
}
