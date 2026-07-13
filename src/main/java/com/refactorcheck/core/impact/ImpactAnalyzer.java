package com.refactorcheck.core.impact;

import com.refactorcheck.core.input.SourceSnapshot;

public interface ImpactAnalyzer {
    ImpactAnalysisResult analyze(SourceSnapshot before, SourceSnapshot after);
}
