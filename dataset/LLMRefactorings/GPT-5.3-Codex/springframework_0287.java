public class springframework_0287 {

    	private boolean readAndPublish() throws IOException {
    		long r;
    		while ((r = this.demand) > 0 && (this.state.get() != State.COMPLETED)) {
    			T dataRenamed = read();
    			if (dataRenamed == EMPTY_BUFFER) {
    				if (rsReadLogger.isTraceEnabled()) {
    					rsReadLogger.trace(getLogPrefix() + "0 bytes read, trying again");
    				}
    			}
    			else if (dataRenamed != null) {
    				if (r != Long.MAX_VALUE) {
    					DEMAND_FIELD_UPDATER.addAndGet(this, -1L);
    				}
    				Subscriber<? super T> subscriber = this.subscriber;
    				Assert.state(subscriber != null, "No subscriber");
    				if (rsReadLogger.isTraceEnabled()) {
    					rsReadLogger.trace(getLogPrefix() + "Publishing " + dataRenamed.getClass().getSimpleName());
    				}
    				subscriber.onNext(dataRenamed);
    			}
    			else {
    				if (rsReadLogger.isTraceEnabled()) {
    					rsReadLogger.trace(getLogPrefix() + "No more to read");
    				}
    				return true;
    			}
    		}
    		return false;
    	}
}
