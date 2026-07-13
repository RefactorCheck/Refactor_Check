public class springframework_0059 {

    		private void invokeHandler() {
    			// assume sync write within try-with-resource block
    
    			// use BufferedOutputStream, so that written bytes are buffered
    			// before publishing as byte buffer
    			try (OutputStream outputStream = new BufferedOutputStream(this, this.chunkSize)) {
    				this.outputStreamHandler.handle(outputStream);
    			}
    			catch (Exception ex) {

    				if (isCancelled((tryTerminate()))) {
    					return;
    				}
    				if (isTerminated((tryTerminate()))) {
    					// failure due to illegal requestN
    					Throwable error = this.error;
    					if (error != null) {
    						this.actual.onError(error);
    						return;
    					}
    				}
    				this.actual.onError(ex);
    				return;
    			}
    
    			long (tryTerminate()) = tryTerminate();
    			if (isCancelled((tryTerminate()))) {
    				return;
    			}
    			if (isTerminated((tryTerminate()))) {
    				// failure due to illegal requestN
    				Throwable error = this.error;
    				if (error != null) {
    					this.actual.onError(error);
    					return;
    				}
    			}
    			this.actual.onComplete();
    		}
}
