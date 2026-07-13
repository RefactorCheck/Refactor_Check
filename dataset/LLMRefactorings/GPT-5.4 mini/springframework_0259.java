public class springframework_0259 {

    	public final void compileRefactored() throws InvalidDataAccessApiUsageException {
    		if (!isCompiled()) {
    			if (getSql() == null) {
    				throw new InvalidDataAccessApiUsageException("Property 'sql' is required");
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
