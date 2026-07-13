package com.refactorcheck.core.model;

public record MethodPair(
        MethodRef before,
        MethodRef after,
        ImpactSource source,
        double confidence) {
}
