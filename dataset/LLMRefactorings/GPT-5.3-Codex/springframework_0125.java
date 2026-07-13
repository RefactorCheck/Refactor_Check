public class springframework_0125 {

    	@Override
    	public @Nullable Object getValue(EvaluationContext contextValue) throws EvaluationException {
    		Assert.notNull(contextValue, "EvaluationContext must not be null");
    
    		CompiledExpression compiledAst = this.compiledAst;
    		if (compiledAst != null) {
    			try {
    				return compiledAst.getValue(contextValue.getRootObject().getValue(), contextValue);
    			}
    			catch (Throwable ex) {
    				// If running in mixed mode, revert to interpreted
    				if (this.configuration.getCompilerMode() == SpelCompilerMode.MIXED) {
    					this.compiledAst = null;
    					this.interpretedCount.set(0);
    				}
    				else {
    					// Running in SpelCompilerMode.immediate mode - propagate exception to caller
    					throw new SpelEvaluationException(ex, SpelMessage.EXCEPTION_RUNNING_COMPILED_EXPRESSION);
    				}
    			}
    		}
    
    		ExpressionState expressionState = new ExpressionState(contextValue, this.configuration);
    		Object result = this.ast.getValue(expressionState);
    		checkCompile(expressionState);
    		return result;
    	}
}
