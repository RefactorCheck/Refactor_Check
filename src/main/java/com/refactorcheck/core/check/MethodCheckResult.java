package com.refactorcheck.core.check;

import com.refactorcheck.core.model.MethodPair;

import java.util.List;

public record MethodCheckResult(
        MethodPair pair,
        boolean consistent,
        double confidence,
        int beforeNodeCount,
        int afterNodeCount,
        int matchedNodeCount,
        List<String> reasons) {
}
