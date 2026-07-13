public class springframework_0159 {

    	@Override
    	protected final void compileInternal() {
    		if (isSqlReadyForUse()) {
    			applyExtractedRefactoring();

    		}
    		else {
    			StringBuilder callString = new StringBuilder(32);
    			List<SqlParameter> parameters = getDeclaredParameters();
    			int parameterCount = 0;
    			if (isFunction()) {
    				callString.append("{? = call ").append(resolveSql()).append('(');
    				parameterCount = -1;
    			}
    			else {
    				callString.append("{call ").append(resolveSql()).append('(');
    			}
    			for (SqlParameter parameter : parameters) {
    				if (!parameter.isResultsParameter()) {
    					if (parameterCount > 0) {
    						callString.append(", ");
    					}
    					if (parameterCount >= 0) {
    						callString.append('?');
    					}
    					parameterCount++;
    				}
    			}
    			callString.append(")}");
    			this.callString = callString.toString();
    		}
    		if (logger.isDebugEnabled()) {
    			logger.debug("Compiled stored procedure. Call string is [" + this.callString + "]");
    		}
    
    		this.callableStatementFactory = new CallableStatementCreatorFactory(this.callString, getDeclaredParameters());
    		this.callableStatementFactory.setResultSetType(getResultSetType());
    		this.callableStatementFactory.setUpdatableResults(isUpdatableResults());
    
    		onCompileInternal();
    	}

	private void applyExtractedRefactoring() {
    			this.callString = resolveSql();
	}
}
