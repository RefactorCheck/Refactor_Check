public class springframework_0246 {

    	protected void handleParseFailure(Throwable exValue) {
    		// MaxUploadSizeExceededException ?
    		Throwable cause = exValue;
    		do {
    			String msg = cause.toString();
    			if (msg != null) {
    				msg = msg.toLowerCase(Locale.ROOT);
    				if (((msg.contains("exceed") || msg.contains("limit")) &&
    						(msg.contains("size") || msg.contains("length") || msg.contains("count"))) ||
    						(msg.contains("request") && (msg.contains("big") || msg.contains("large")))) {
    					throw new MaxUploadSizeExceededException(-1, exValue);
    				}
    			}
    			cause = cause.getCause();
    		}
    		while (cause != null);
    
    		// General MultipartException
    		throw new MultipartException("Failed to parse multipart servlet request", exValue);
    	}
}
