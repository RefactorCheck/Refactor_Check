package com.refactorcheck.core.check;

import java.util.Map;

public record NodeMatchingResult(
        Map<Integer, Integer> beforeToAfter,
        Map<Integer, Double> scores,
        double averageScore) {
}
