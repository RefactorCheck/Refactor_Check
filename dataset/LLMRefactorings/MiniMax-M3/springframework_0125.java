public class springframework_0125 {

    	@Override
    	public @Nullable Object getValue(EvaluationContext context) throws EvaluationException {
    		Assert.notNull(context, "EvaluationContext must not be null");
    
    		CompiledExpression compiledAst = this.compiledAst;
    		if (compiledAst != null) {
    			try {
    				return compiledAst.getValue(context.getRootObject().getValue(), context);
    			}
    			catch (Throwable ex) {
    				if (this.configuration.getCompilerMode() == SpelCompilerMode.MIXED) {
    					this.compiledAst = null;
    					this.interpretedCount.set(0);
    				}
    				else {
    					throw new SpelEvaluationException(ex, SpelMessage.EXCEPTION_RUNNING_COMPILED_EXPRESSION);
    				}
    			}
    		}
    
    		return getInterpretedValue(context);
    	}
    
    	private Object getInterpretedValue(EvaluationContext context) {
    		ExpressionState expressionState = new ExpressionState(context, this.configuration);
    		Object result = this.ast.getValue(expressionState);
    		checkCompile(expressionState);
    		return result;
    	}
}
