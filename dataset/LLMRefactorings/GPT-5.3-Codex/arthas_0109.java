public class arthas_0109 {

        private static FallbackDiagnostics diagnoseFallbackMappingRefactored(StatsRow stats, List<LoaderMapping> fallbackMappings) {
            LoaderMapping matched = null;
            FallbackDiagnostics diagnostics = new FallbackDiagnostics(stats);
            for (LoaderMapping mapping : fallbackMappings) {
                diagnostics.totalCandidateCount++;
                if (!mapping.matchesType(stats.typeName)) {
                    continue;
                }
    
                diagnostics.sameTypeCandidateCount++;
                diagnostics.addNearestCandidate(mapping);
                if (mapping.loadedClassCount == stats.totalClassCountForMapping()) {
                    diagnostics.exactClassCountCandidateCount++;
                    diagnostics.addExactCandidate(mapping);
                    if (matched != null) {
                        diagnostics.ambiguous = true;
                        diagnostics.mapping = null;
                        continue;
                    }
                    matched = mapping;
                    diagnostics.mapping = mapping;
                }
            }
            if (diagnostics.ambiguous) {
                diagnostics.mapping = null;
            }
            return diagnostics;
        }
}
