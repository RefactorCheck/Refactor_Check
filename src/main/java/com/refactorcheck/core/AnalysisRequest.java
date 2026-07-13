package com.refactorcheck.core;

import com.refactorcheck.core.model.ImpactMode;

import java.nio.file.Path;

public record AnalysisRequest(
        Path before,
        Path after,
        ImpactMode impactMode,
        Path mapper,
        Path output) {
}
