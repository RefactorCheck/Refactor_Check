package com.refactorcheck.core.impact;

import com.refactorcheck.core.input.SourceSnapshot;

public final class RefactoringMinerImpactAnalyzer implements ImpactAnalyzer {
    @Override
    public ImpactAnalysisResult analyze(SourceSnapshot before, SourceSnapshot after) {
        throw new UnsupportedOperationException("RefactoringMiner fallback is reserved for future implementation.");
    }
}
