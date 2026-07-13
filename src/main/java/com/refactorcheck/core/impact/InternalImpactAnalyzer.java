package com.refactorcheck.core.impact;

import com.refactorcheck.core.input.SourceSnapshot;
import com.refactorcheck.core.model.ImpactSource;
import com.refactorcheck.core.model.MethodPair;

import java.util.List;

public final class InternalImpactAnalyzer implements ImpactAnalyzer {

    @Override
    public ImpactAnalysisResult analyze(SourceSnapshot before, SourceSnapshot after) {
        List<MethodPair> mapped = ImpactUtils.internalGreedyMapping(before, after, ImpactSource.INTERNAL_FALLBACK);
        if (mapped.isEmpty()) {
            return new ImpactAnalysisResult(
                    List.of(),
                    "INTERNAL_FALLBACK",
                    true,
                    List.of("Internal fallback analyzer could not map any methods."));
        }

        List<MethodPair> impacted = ImpactUtils.changedSubset(mapped, before, after);
        List<MethodPair> expanded = ImpactUtils.expandWithCallers(before, after, impacted, ImpactSource.CALLER_EXPANSION);
        return new ImpactAnalysisResult(expanded, "INTERNAL_FALLBACK", true, List.of());
    }
}
