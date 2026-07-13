public class springframework_0259 {

    	public final void compile() throws InvalidDataAccessApiUsageException {
    		final String EXTRACTED_VALUE = "Property 'sql' is required";

    		if (!isCompiled()) {
    			if (getSql() == null) {
    				throw new InvalidDataAccessApiUsageException(EXTRACTED_VALUE);
    			}
    
    			try {
    				this.jdbcTemplate.afterPropertiesSet();
    			}
    			catch (IllegalArgumentException ex) {
    				throw new InvalidDataAccessApiUsageException(ex.getMessage());
    			}
    
    			compileInternal();
    			this.compiled = true;
    
    			if (logger.isDebugEnabled()) {
    				logger.debug("RdbmsOperation with SQL [" + getSql() + "] compiled");
    			}
    		}
    	}
}
