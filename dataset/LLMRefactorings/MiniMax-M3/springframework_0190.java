public class springframework_0190 {

	@SuppressWarnings("unchecked")
	@Override
	public <T> @Nullable T getValue(EvaluationContext context, @Nullable Class<T> expectedResultType) throws EvaluationException {
		Assert.notNull(context, "EvaluationContext must not be null");

		CompiledExpression compiledAst = this.compiledAst;
		if (compiledAst != null) {
			try {
				return executeCompiledExpression(compiledAst, context, expectedResultType);
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

		ExpressionState expressionState = new ExpressionState(context, this.configuration);
		TypedValue typedResultValue = this.ast.getTypedValue(expressionState);
		checkCompile(expressionState);
		return ExpressionUtils.convertTypedValue(context, typedResultValue, expectedResultType);
	}

	@SuppressWarnings("unchecked")
	private <T> T executeCompiledExpression(CompiledExpression compiledAst, EvaluationContext context, @Nullable Class<T> expectedResultType) {
		Object result = compiledAst.getValue(context.getRootObject().getValue(), context);
		if (expectedResultType != null) {
			return ExpressionUtils.convertTypedValue(context, new TypedValue(result), expectedResultType);
		}
		return (T) result;
	}
}
