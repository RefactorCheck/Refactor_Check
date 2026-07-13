public class springframework_0085 {

    public boolean compileExpression() {
        if (this.compiledAst != null) {
            return true;
        }
        if (this.failedAttempts.get() > FAILED_ATTEMPTS_THRESHOLD) {
            return false;
        }

        synchronized (this) {
            if (this.compiledAst != null) {
                return true;
            }
            return performCompilation();
        }
    }

    private boolean performCompilation() {
        try {
            SpelCompiler compiler = SpelCompiler.getCompiler(this.configuration.getCompilerClassLoader());
            CompiledExpression compiledAst = compiler.compile(this.ast);
            if (compiledAst != null) {
                this.compiledAst = compiledAst;
                return true;
            }
            else {
                this.failedAttempts.incrementAndGet();
                return false;
            }
        }
        catch (Exception ex) {
            this.failedAttempts.incrementAndGet();

            if (this.configuration.getCompilerMode() == SpelCompilerMode.MIXED) {
                this.compiledAst = null;
                this.interpretedCount.set(0);
                return false;
            }
            else {
                throw new SpelEvaluationException(ex, SpelMessage.EXCEPTION_COMPILING_EXPRESSION);
            }
        }
    }
}
