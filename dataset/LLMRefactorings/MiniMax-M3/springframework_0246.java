public class springframework_0246 {

    	protected void handleParseFailure(Throwable ex) {
    		// MaxUploadSizeExceededException ?
    		Throwable cause = ex;
    		do {
    			String msg = cause.toString();
    			if (isUploadSizeExceeded(msg)) {
    				throw new MaxUploadSizeExceededException(-1, ex);
    			}
    			cause = cause.getCause();
    		}
    		while (cause != null);
    
    		// General MultipartException
    		throw new MultipartException("Failed to parse multipart servlet request", ex);
    	}
    	
    	private boolean isUploadSizeExceeded(String msg) {
    		if (msg != null) {
				msg = msg.toLowerCase(Locale.ROOT);
				return ((msg.contains("exceed") || msg.contains("limit")) &&
						(msg.contains("size") || msg.contains("length") || msg.contains("count"))) ||
						(msg.contains("request") && (msg.contains("big") || msg.contains("large")));
			}
			return false;
    	}
}
