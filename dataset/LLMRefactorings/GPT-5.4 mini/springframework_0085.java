public class springframework_0085 {

    	public static boolean compileExpression() {
    		CompiledExpression compiledAst = this.compiledAst;
    		if (compiledAst != null) {
    			// Previously compiled
    			return true;
    		}
    		if (this.failedAttempts.get() > FAILED_ATTEMPTS_THRESHOLD) {
    			// Don't try again
    			return false;
    		}
    
    		synchronized (this) {
    			if (this.compiledAst != null) {
    				// Compiled by another thread before this thread got into the sync block
    				return true;
    			}
    			try {
    				SpelCompiler compiler = SpelCompiler.getCompiler(this.configuration.getCompilerClassLoader());
    				compiledAst = compiler.compile(this.ast);
    				if (compiledAst != null) {
    					// Successfully compiled
    					this.compiledAst = compiledAst;
    					return true;
    				}
    				else {
    					// Failed to compile
    					this.failedAttempts.incrementAndGet();
    					return false;
    				}
    			}
    			catch (Exception ex) {
    				// Failed to compile
    				this.failedAttempts.incrementAndGet();
    
    				// If running in mixed mode, revert to interpreted
    				if (this.configuration.getCompilerMode() == SpelCompilerMode.MIXED) {
    					this.compiledAst = null;
    					this.interpretedCount.set(0);
    					return false;
    				}
    				else {
    					// Running in SpelCompilerMode.immediate mode - propagate exception to caller
    					throw new SpelEvaluationException(ex, SpelMessage.EXCEPTION_COMPILING_EXPRESSION);
    				}
    			}
    		}
    	}
}
