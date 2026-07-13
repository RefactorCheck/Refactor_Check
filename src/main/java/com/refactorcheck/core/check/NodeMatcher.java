package com.refactorcheck.core.check;

import com.refactorcheck.core.model.CfgNode;
import com.refactorcheck.core.model.MethodCfg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public final class NodeMatcher {

    public NodeMatchingResult match(MethodCfg before, MethodCfg after) {
        Map<Integer, Integer> mapping = new HashMap<>();
        Map<Integer, Double> scores = new HashMap<>();
        Set<Integer> usedAfter = new HashSet<>();

        for (CfgNode left : before.nodes()) {
            double bestScore = 0.0;
            Integer bestRight = null;

            for (CfgNode right : after.nodes()) {
                if (usedAfter.contains(right.index())) {
                    continue;
                }
                double score = similarity(left, right);
                if (score > bestScore) {
                    bestScore = score;
                    bestRight = right.index();
                }
            }

            if (bestRight != null && bestScore >= 0.45) {
                mapping.put(left.index(), bestRight);
                scores.put(left.index(), bestScore);
                usedAfter.add(bestRight);
            }
        }

        double avg = scores.values().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        return new NodeMatchingResult(mapping, scores, avg);
    }

    private double similarity(CfgNode left, CfgNode right) {
        if (left.text().equals(right.text())) {
            return 1.0;
        }

        double score = 0.0;
        Set<String> leftWordTokens = wordTokens(left.text());
        Set<String> rightWordTokens = wordTokens(right.text());
        Set<String> leftOperators = operatorTokens(left.text());
        Set<String> rightOperators = operatorTokens(right.text());

        score += 0.30 * jaccard(leftWordTokens, rightWordTokens);
        score += 0.15 * jaccard(leftOperators, rightOperators);
        if (left.kind() == right.kind()) {
            score += 0.20;
        }
        score += 0.15 * jaccard(left.usedIdentifiers(), right.usedIdentifiers());
        score += 0.10 * jaccard(left.definedIdentifiers(), right.definedIdentifiers());
        score += 0.10 * jaccard(left.literals(), right.literals());

        if (!leftOperators.equals(rightOperators)
                && !left.usedIdentifiers().isEmpty()
                && left.usedIdentifiers().equals(right.usedIdentifiers())) {
            score = Math.min(score, 0.58);
        }

        return Math.min(score, 1.0);
    }

    private Set<String> wordTokens(String text) {
        String[] split = text.toLowerCase(Locale.ROOT).split("[^a-z0-9_]+");
        Set<String> tokens = new HashSet<>();
        for (String token : split) {
            if (!token.isBlank()) {
                tokens.add(token);
            }
        }
        if (text.contains("this")) {
            tokens.add("this");
        }
        if (text.contains("super")) {
            tokens.add("super");
        }
        return tokens;
    }

    private Set<String> operatorTokens(String text) {
        Set<String> operators = new HashSet<>();
        String[] candidates = {"==", "!=", ">=", "<=", "&&", "||", "++", "--", "+=", "-=", "*=", "/=", "%=", "->", "::",
                "!", "+", "-", "*", "/", "%", "=", ">", "<"};
        for (String candidate : candidates) {
            if (text.contains(candidate)) {
                operators.add(candidate);
            }
        }
        return operators;
    }

    private double jaccard(Set<String> left, Set<String> right) {
        if (left.isEmpty() && right.isEmpty()) {
            return 1.0;
        }
        Set<String> intersection = new HashSet<>(left);
        intersection.retainAll(right);
        Set<String> union = new HashSet<>(left);
        union.addAll(right);
        if (union.isEmpty()) {
            return 0.0;
        }
        return (double) intersection.size() / (double) union.size();
    }
}
