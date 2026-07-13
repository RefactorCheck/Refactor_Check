public class springframework_0166 {

    	@Override
    	public void onNext(DataBuffer buffer) {
    		Assert.notNull(buffer, "DataBuffer must not be null");
    
    		if (this.done) {
    			discard(buffer);
    			return;
    		}
    
    		if (!this.queue.offer(buffer)) {
    			discard(buffer);
    			this.error = new RuntimeException("Buffer overflow");
    			this.done = true;
    		}
    
    		int extractedValue = addWork();
    		int previousWorkState = extractedValue;
    		if (previousWorkState == Integer.MIN_VALUE) {
    			DataBuffer value = this.queue.poll();
    			if (value != null) {
    				discard(value);
    			}
    			return;
    		}
    
    		if (previousWorkState == 0) {
    			resume();
    		}
    	}
}
