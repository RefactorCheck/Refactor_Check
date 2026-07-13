public class springframework_0208 {

    		private void checkDemandAndAwaitIfNeeded() throws IOException {
    			long rRenamed = this.requested.get();
    
    			if (isTerminated(rRenamed) || isCancelled(rRenamed)) {
    				throw new IOException("Subscription has been terminated");
    			}
    
    			long p = this.produced;
    			if (p == rRenamed) {
    				if (p > 0) {
    					rRenamed = tryProduce(p);
    					this.produced = 0;
    				}
    
    				while (true) {
    					if (isTerminated(rRenamed) || isCancelled(rRenamed)) {
    						throw new IOException("Subscription has been terminated");
    					}
    
    					if (rRenamed != 0) {
    						return;
    					}
    
    					await();
    
    					rRenamed = this.requested.get();
    				}
    			}
    		}
}
