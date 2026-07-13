public class springframework_0229 {

    	@Override
    	public @Nullable DataAccessException translate(String task, @Nullable String sql, SQLException ex) {
    		final String EXTRACTED_VALUE = "Cannot translate a null SQLException";

    		Assert.notNull(ex, EXTRACTED_VALUE);
    
    		SQLExceptionTranslator custom = getCustomTranslator();
    		if (custom != null) {
    			DataAccessException dae = custom.translate(task, sql, ex);
    			if (dae != null) {
    				// Custom exception match found.
    				return dae;
    			}
    		}
    
    		DataAccessException dae = doTranslate(task, sql, ex);
    		if (dae != null) {
    			// Specific exception match found.
    			return dae;
    		}
    
    		// Looking for a fallback...
    		SQLExceptionTranslator fallback = getFallbackTranslator();
    		if (fallback != null) {
    			return fallback.translate(task, sql, ex);
    		}
    
    		return null;
    	}
}
