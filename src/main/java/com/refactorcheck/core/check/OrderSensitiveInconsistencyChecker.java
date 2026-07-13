package com.refactorcheck.core.check;

import com.refactorcheck.core.model.CfgNode;
import com.refactorcheck.core.model.ExceptionKind;
import com.refactorcheck.core.model.ImpactSource;
import com.refactorcheck.core.model.MethodCfg;
import com.refactorcheck.core.model.MethodPair;
import com.refactorcheck.core.model.NodeKind;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class OrderSensitiveInconsistencyChecker {
    private static final boolean ENABLE_EXCEPTION_CONSISTENCY_CHECKS = false;
    private static final boolean ENABLE_UNMATCHED_IMPACT_CHECKS = true;
    private static final boolean ENABLE_BROAD_UNMATCHED_IMPACT_CHECKS = false;
    private static final boolean ENABLE_MAPPED_DRIFT_CHECKS = false;
    private static final double MAPPED_DRIFT_SCORE_THRESHOLD = 0.60;
    private static final Set<ExceptionKind> CRITICAL_EXCEPTION_KINDS = Set.of(
            ExceptionKind.INDEX_OUT_OF_BOUNDS,
            ExceptionKind.ARITHMETIC,
            ExceptionKind.CLASS_CAST,
            ExceptionKind.NUMBER_FORMAT,
            ExceptionKind.ILLEGAL_STATE,
            ExceptionKind.ILLEGAL_ARGUMENT);

    public MethodCheckResult check(
            MethodPair pair,
            MethodCfg before,
            MethodCfg after,
            NodeMatchingResult matching) {
        List<String> reasons = new ArrayList<>();
        Map<Integer, CfgNode> beforeNodes = index(before.nodes());
        Map<Integer, CfgNode> afterNodes = index(after.nodes());

        if (ENABLE_EXCEPTION_CONSISTENCY_CHECKS) {
            validateExceptionConsistency(matching, beforeNodes, afterNodes, reasons);
        }
        if (ENABLE_UNMATCHED_IMPACT_CHECKS) {
            validateUnmatchedImpact(pair, before, after, matching, reasons);
        }
        if (ENABLE_MAPPED_DRIFT_CHECKS) {
            validateMappedSemanticDrift(matching, beforeNodes, afterNodes, reasons);
        }

        double coverage = before.nodes().isEmpty()
                ? 1.0
                : (double) matching.beforeToAfter().size() / (double) before.nodes().size();
        double confidence = Math.max(0.0, Math.min(1.0, matching.averageScore() * coverage));

        return new MethodCheckResult(
                pair,
                reasons.isEmpty(),
                confidence,
                before.nodes().size(),
                after.nodes().size(),
                matching.beforeToAfter().size(),
                List.copyOf(reasons));
    }

    private void validateExceptionConsistency(
            NodeMatchingResult matching,
            Map<Integer, CfgNode> beforeNodes,
            Map<Integer, CfgNode> afterNodes,
            List<String> reasons) {
        int mismatches = 0;
        int firstBeforeLine = -1;
        int firstAfterLine = -1;

        for (Map.Entry<Integer, Integer> entry : matching.beforeToAfter().entrySet()) {
            CfgNode left = beforeNodes.get(entry.getKey());
            CfgNode right = afterNodes.get(entry.getValue());
            if (left == null || right == null) {
                continue;
            }
            Set<ExceptionKind> leftCritical = criticalExceptionKinds(left);
            Set<ExceptionKind> rightCritical = criticalExceptionKinds(right);
            if (!leftCritical.equals(rightCritical)) {
                mismatches++;
                if (firstBeforeLine < 0) {
                    firstBeforeLine = left.line();
                    firstAfterLine = right.line();
                }
            }
        }

        int mapped = matching.beforeToAfter().size();
        if (mapped == 0) {
            return;
        }
        double ratio = (double) mismatches / (double) mapped;
        if (firstBeforeLine == firstAfterLine) {
            return;
        }
        if (mismatches >= 2 && ratio >= 0.20) {
            reasons.add("Exception annotation mismatch at before line " + firstBeforeLine + " and after line " + firstAfterLine);
        }
    }

    private void validateUnmatchedImpact(
            MethodPair pair,
            MethodCfg before,
            MethodCfg after,
            NodeMatchingResult matching,
            List<String> reasons) {
        Set<Integer> matchedBefore = matching.beforeToAfter().keySet();
        Set<Integer> matchedAfter = new HashSet<>(matching.beforeToAfter().values());

        long impactfulRemoved = before.nodes().stream()
                .filter(node -> !matchedBefore.contains(node.index()))
                .filter(this::isStrongBehaviorChanging)
                .count();
        long impactfulAdded = after.nodes().stream()
                .filter(node -> !matchedAfter.contains(node.index()))
                .filter(this::isStrongBehaviorChanging)
                .count();
        long impactfulTotal = impactfulRemoved + impactfulAdded;
        long totalNodes = Math.max(before.nodes().size(), after.nodes().size());
        long trigger = Math.max(9L, (long) Math.ceil(totalNodes * 0.35));

        if (ENABLE_BROAD_UNMATCHED_IMPACT_CHECKS && impactfulTotal >= trigger) {
            reasons.add("Behavior-impacting unmatched nodes detected (removed=" + impactfulRemoved + ", added=" + impactfulAdded + ")");
        }

        if (isAnonymousNestedMethodRemap(pair, before, after, matchedBefore, matchedAfter)) {
            reasons.add("Unmatched anonymous/nested method body under non-identical method mapping can change binding.");
        }
    }

    private boolean isAnonymousNestedMethodRemap(
            MethodPair pair,
            MethodCfg before,
            MethodCfg after,
            Set<Integer> matchedBefore,
            Set<Integer> matchedAfter) {
        if (pair.source() != ImpactSource.REEXTRACTOR) {
            return false;
        }
        if (!pair.before().container().equals(pair.after().container())) {
            return false;
        }
        if (pair.before().signature().equals(pair.after().signature())) {
            return false;
        }
        if (!looksLikeExistingEnclosingMethod(pair)) {
            return false;
        }
        if (before.nodes().size() > 2 || after.nodes().size() < before.nodes().size() + 2) {
            return false;
        }
        long unmatchedRemoved = before.nodes().stream()
                .filter(node -> !matchedBefore.contains(node.index()))
                .count();
        if (unmatchedRemoved > 0) {
            return false;
        }

        String originalMethodToken = pair.before().methodName() + "(";
        return after.nodes().stream()
                .filter(node -> !matchedAfter.contains(node.index()))
                .filter(this::isStrongBehaviorChanging)
                .map(CfgNode::text)
                .anyMatch(text -> looksLikeAnonymousOrNestedMethodBody(text, originalMethodToken));
    }

    private boolean looksLikeExistingEnclosingMethod(MethodPair pair) {
        if (pair.after().startLine() > pair.before().startLine()
                || pair.after().endLine() < pair.before().endLine()) {
            return false;
        }
        String afterName = pair.after().methodName();
        if ("main".equals(afterName)) {
            return true;
        }
        return !isLikelyExtractedHelperName(afterName);
    }

    private boolean isLikelyExtractedHelperName(String methodName) {
        String lower = methodName.toLowerCase(java.util.Locale.ROOT);
        String[] helperPrefixes = {
                "create", "new", "build", "make", "generate", "extract", "prepare", "init",
                "compute", "resolve", "handle", "process", "get", "set", "to", "as"
        };
        for (String prefix : helperPrefixes) {
            if (lower.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    private boolean looksLikeAnonymousOrNestedMethodBody(String text, String originalMethodToken) {
        String compact = text.replaceAll("\\s+", " ");
        return compact.contains("new ")
                && compact.contains("{")
                && compact.contains("}")
                && compact.contains(originalMethodToken);
    }

    private void validateMappedSemanticDrift(
            NodeMatchingResult matching,
            Map<Integer, CfgNode> beforeNodes,
            Map<Integer, CfgNode> afterNodes,
            List<String> reasons) {
        int drifted = 0;
        for (Map.Entry<Integer, Integer> entry : matching.beforeToAfter().entrySet()) {
            CfgNode left = beforeNodes.get(entry.getKey());
            CfgNode right = afterNodes.get(entry.getValue());
            if (left == null || right == null) {
                continue;
            }
            if (!isSemanticCritical(left) && !isSemanticCritical(right)) {
                continue;
            }
            double score = matching.scores().getOrDefault(entry.getKey(), 0.0);
            if (score < MAPPED_DRIFT_SCORE_THRESHOLD) {
                if (hasExplicitCast(right.text())) {
                    continue;
                }
                drifted++;
            }
        }
        if (drifted < 3) {
            return;
        }
        if (drifted > 0) {
            reasons.add("Low-confidence semantic drift detected for " + drifted + " mapped nodes");
        }
    }

    private boolean isStrongBehaviorChanging(CfgNode node) {
        return node.controlPoint()
                || node.ioOperation()
                || node.externalCall()
                || node.kind() == NodeKind.RETURN
                || node.kind() == NodeKind.THROW
                || !criticalExceptionKinds(node).isEmpty();
    }

    private boolean isSemanticCritical(CfgNode node) {
        return node.kind() == NodeKind.ASSIGNMENT
                || node.kind() == NodeKind.RETURN
                || node.kind() == NodeKind.THROW
                || node.kind() == NodeKind.METHOD_CALL
                || node.kind() == NodeKind.IF
                || node.kind() == NodeKind.LOOP
                || node.kind() == NodeKind.SWITCH
                || node.controlPoint()
                || node.externalCall()
                || node.ioOperation();
    }

    private Set<ExceptionKind> criticalExceptionKinds(CfgNode node) {
        Set<ExceptionKind> filtered = new HashSet<>(node.exceptionKinds());
        filtered.retainAll(CRITICAL_EXCEPTION_KINDS);
        return filtered;
    }

    private boolean hasExplicitCast(String text) {
        return text.matches(".*\\(\\s*(byte|short|int|long|float|double|char|boolean|[A-Z][A-Za-z0-9_$.]*)\\s*\\)\\s*['\"A-Za-z0-9_(].*");
    }

    private Map<Integer, CfgNode> index(List<CfgNode> nodes) {
        Map<Integer, CfgNode> map = new HashMap<>();
        for (CfgNode node : nodes) {
            map.put(node.index(), node);
        }
        return map;
    }
}
