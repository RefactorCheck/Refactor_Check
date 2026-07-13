public class springframework_0074 {

    	@SuppressWarnings("unchecked")
    	@Override
    	public <T> @Nullable T getValue(@Nullable Object rootObject, @Nullable Class<T> expectedResultType) throws EvaluationException {
    		CompiledExpression compiledAst = this.compiledAst;
    		if (compiledAst != null) {
    			try {
    				Object result = compiledAst.getValue(rootObject, getEvaluationContext());
    				if (expectedResultType == null) {
    					return (T)result;
    				}
    				else {
    					return ExpressionUtils.convertTypedValue(
    							getEvaluationContext(), new TypedValue(result), expectedResultType);
    				}
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
    
    		ExpressionState expressionState =
    				new ExpressionState(getEvaluationContext(), toTypedValue(rootObject), this.configuration);
    		TypedValue typedResultValue = this.ast.getTypedValue(expressionState);
    		checkCompile(expressionState);
    		return ExpressionUtils.convertTypedValue(
    				expressionState.getEvaluationContext(), typedResultValue, expectedResultType);
    	}
}
