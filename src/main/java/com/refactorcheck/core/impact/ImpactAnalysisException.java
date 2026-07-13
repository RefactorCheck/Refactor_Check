package com.refactorcheck.core.impact;

public class ImpactAnalysisException extends RuntimeException {
    public ImpactAnalysisException(String message) {
        super(message);
    }

    public ImpactAnalysisException(String message, Throwable cause) {
        super(message, cause);
    }
}
