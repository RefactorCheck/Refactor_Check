package com.refactorcheck.core.impact;

import com.refactorcheck.core.model.MethodPair;

import java.util.List;

public record ImpactAnalysisResult(
        List<MethodPair> methodPairs,
        String provider,
        boolean fallback,
        List<String> warnings) {
}
