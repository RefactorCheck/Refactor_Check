public class springframework_0259 {

    	public final void compile() throws InvalidDataAccessApiUsageException {
    		if (!isCompiled()) {
                validateSql();
    			
    			try {
    				this.jdbcTemplate.afterPropertiesSet();
    			}
    			catch (IllegalArgumentException ex) {
    				throw new InvalidDataAccessApiUsageException(ex.getMessage());
    			}
    
    			compileInternal();
    			this.compiled = true;
    
                logCompilation();
    		}
    	}

        private void validateSql() {
            if (getSql() == null) {
                throw new InvalidDataAccessApiUsageException("Property 'sql' is required");
            }
        }

        private void logCompilation() {
            if (logger.isDebugEnabled()) {
                logger.debug("RdbmsOperation with SQL [" + getSql() + "] compiled");
            }
        }
}
