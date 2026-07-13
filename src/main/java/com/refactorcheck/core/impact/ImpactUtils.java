package com.refactorcheck.core.impact;

import com.refactorcheck.core.analysis.JavaModelBuilder;
import com.refactorcheck.core.input.SourceSnapshot;
import com.refactorcheck.core.mapper.MapperSpec;
import com.refactorcheck.core.model.CallSite;
import com.refactorcheck.core.model.ImpactSource;
import com.refactorcheck.core.model.MethodModel;
import com.refactorcheck.core.model.MethodPair;
import com.refactorcheck.core.model.MethodRef;
import com.refactorcheck.core.model.MethodSignature;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

final class ImpactUtils {
    private ImpactUtils() {
    }

    static Optional<MethodModel> resolveByMapperSpec(SourceSnapshot snapshot, MapperSpec.MapperMethod methodSpec) {
        String signatureNormalized = MethodSignature.normalizeSignature(methodSpec.signature());
        String normalizedFileInput = normalizePath(methodSpec.file());
        Path absoluteInput = Path.of(methodSpec.file()).toAbsolutePath().normalize();

        List<MethodModel> candidates = snapshot.methods().stream()
                .filter(model -> methodMatchesPath(model.ref(), normalizedFileInput, absoluteInput.toString()))
                .toList();

        Optional<MethodModel> exact = candidates.stream()
                .filter(model -> MethodSignature.normalizeSignature(model.ref().signature()).equals(signatureNormalized))
                .findFirst();
        if (exact.isPresent()) {
            return exact;
        }

        return snapshot.methods().stream()
                .filter(model -> MethodSignature.normalizeSignature(model.ref().signature()).equals(signatureNormalized))
                .findFirst();
    }

    static Optional<MethodModel> resolveByLocation(
            SourceSnapshot snapshot,
            String filePath,
            int startLine,
            int endLine,
            String methodName,
            int arity) {
        String normalizedFileInput = normalizePath(filePath);

        List<MethodModel> byPath = snapshot.methods().stream()
                .filter(model -> methodMatchesPath(model.ref(), normalizedFileInput, null))
                .toList();
        List<MethodModel> scope = byPath.isEmpty() ? new ArrayList<>(snapshot.methods()) : byPath;

        List<MethodModel> byName = scope.stream()
                .filter(model -> methodName == null || model.ref().methodName().equals(methodName))
                .filter(model -> arity < 0 || model.ref().arity() == arity)
                .toList();

        List<MethodModel> candidates = byName.isEmpty() ? scope : byName;

        if (startLine > 0) {
            List<MethodModel> containing = candidates.stream()
                    .filter(model -> model.ref().startLine() <= startLine && model.ref().endLine() >= startLine)
                    .toList();
            if (!containing.isEmpty()) {
                return containing.stream()
                        .min(Comparator.comparingInt(model -> Math.abs(model.ref().startLine() - startLine)));
            }
        }

        if (endLine > 0) {
            List<MethodModel> containing = candidates.stream()
                    .filter(model -> model.ref().startLine() <= endLine && model.ref().endLine() >= endLine)
                    .toList();
            if (!containing.isEmpty()) {
                return containing.stream()
                        .min(Comparator.comparingInt(model -> Math.abs(model.ref().startLine() - endLine)));
            }
        }

        if (startLine > 0) {
            return candidates.stream()
                    .min(Comparator.comparingInt(model -> Math.abs(model.ref().startLine() - startLine)));
        }
        return candidates.stream().findFirst();
    }

    static List<MethodModel> findDirectCallers(SourceSnapshot snapshot, MethodRef callee) {
        List<MethodModel> callers = new ArrayList<>();
        for (MethodModel method : snapshot.methods()) {
            if (method.ref().methodKey().equals(callee.methodKey())) {
                continue;
            }
            for (CallSite callSite : method.callSites()) {
                if (callSite.methodName().equals(callee.methodName()) && callSite.arity() == callee.arity()) {
                    callers.add(method);
                    break;
                }
            }
        }
        return callers;
    }

    static List<MethodPair> expandWithCallers(
            SourceSnapshot before,
            SourceSnapshot after,
            List<MethodPair> seedPairs,
            ImpactSource sourceForExpandedPairs) {
        Map<String, MethodPair> unique = new HashMap<>();
        for (MethodPair pair : seedPairs) {
            unique.put(pair.before().methodKey() + "->" + pair.after().methodKey(), pair);
        }

        Map<String, MethodRef> mapping = new HashMap<>();
        for (MethodPair pair : seedPairs) {
            mapping.put(pair.before().methodKey(), pair.after());
        }

        for (MethodPair pair : seedPairs) {
            List<MethodModel> beforeCallers = findDirectCallers(before, pair.before());
            List<MethodModel> afterCallers = findDirectCallers(after, pair.after());
            Map<String, MethodModel> afterByKey = new HashMap<>();
            for (MethodModel model : afterCallers) {
                afterByKey.put(model.ref().methodKey(), model);
            }

            for (MethodModel beforeCaller : beforeCallers) {
                MethodRef mapped = mapping.get(beforeCaller.ref().methodKey());
                if (mapped != null && afterByKey.containsKey(mapped.methodKey())) {
                    String key = beforeCaller.ref().methodKey() + "->" + mapped.methodKey();
                    unique.putIfAbsent(key, new MethodPair(beforeCaller.ref(), mapped, sourceForExpandedPairs, pair.confidence() * 0.95));
                    continue;
                }

                Optional<MethodModel> fallback = afterCallers.stream()
                        .filter(candidate -> candidate.ref().methodName().equals(beforeCaller.ref().methodName()))
                        .filter(candidate -> candidate.ref().arity() == beforeCaller.ref().arity())
                        .findFirst();
                if (fallback.isPresent()) {
                    MethodRef afterRef = fallback.get().ref();
                    String key = beforeCaller.ref().methodKey() + "->" + afterRef.methodKey();
                    unique.putIfAbsent(key, new MethodPair(beforeCaller.ref(), afterRef, sourceForExpandedPairs, 0.55));
                }
            }
        }

        return new ArrayList<>(unique.values());
    }

    static List<MethodPair> internalGreedyMapping(SourceSnapshot before, SourceSnapshot after, ImpactSource source) {
        List<Candidate> candidates = new ArrayList<>();
        List<MethodModel> left = new ArrayList<>(before.methods());

        for (MethodModel leftModel : left) {
            List<MethodModel> rightCandidates = candidatePool(after, leftModel);
            for (MethodModel rightModel : rightCandidates) {
                double score = score(leftModel, rightModel);
                if (score >= 0.35) {
                    candidates.add(new Candidate(leftModel, rightModel, score));
                }
            }
        }

        candidates.sort(Comparator.comparingDouble(Candidate::score).reversed());
        Set<String> usedLeft = new HashSet<>();
        Set<String> usedRight = new HashSet<>();
        List<MethodPair> pairs = new ArrayList<>();

        for (Candidate candidate : candidates) {
            String leftKey = candidate.left().ref().methodKey();
            String rightKey = candidate.right().ref().methodKey();
            if (usedLeft.contains(leftKey) || usedRight.contains(rightKey)) {
                continue;
            }
            usedLeft.add(leftKey);
            usedRight.add(rightKey);
            pairs.add(new MethodPair(candidate.left().ref(), candidate.right().ref(), source, candidate.score()));
        }

        return pairs;
    }

    static List<MethodPair> changedSubset(List<MethodPair> allPairs, SourceSnapshot before, SourceSnapshot after) {
        Map<String, MethodModel> beforeByKey = before.methodsByKey();
        Map<String, MethodModel> afterByKey = after.methodsByKey();
        List<MethodPair> changed = new ArrayList<>();
        for (MethodPair pair : allPairs) {
            MethodModel left = beforeByKey.get(pair.before().methodKey());
            MethodModel right = afterByKey.get(pair.after().methodKey());
            if (left == null || right == null) {
                changed.add(pair);
                continue;
            }
            String leftBody = left.bodyText().replaceAll("\\s+", " ").trim();
            String rightBody = right.bodyText().replaceAll("\\s+", " ").trim();
            if (!Objects.equals(leftBody, rightBody)
                    || !MethodSignature.normalizeSignature(pair.before().signature())
                    .equals(MethodSignature.normalizeSignature(pair.after().signature()))) {
                changed.add(pair);
            }
        }
        return changed.isEmpty() ? allPairs : changed;
    }

    private static List<MethodModel> candidatePool(SourceSnapshot after, MethodModel leftModel) {
        List<MethodModel> byArity = after.findMethodsByNameAndArity(leftModel.ref().methodName(), leftModel.ref().arity());
        if (!byArity.isEmpty()) {
            return byArity;
        }

        List<MethodModel> byPath = after.methods().stream()
                .filter(model -> model.ref().relativePath().equals(leftModel.ref().relativePath()))
                .toList();
        if (!byPath.isEmpty()) {
            return byPath;
        }

        return after.methods().stream()
                .filter(model -> model.ref().arity() == leftModel.ref().arity())
                .toList();
    }

    private static double score(MethodModel left, MethodModel right) {
        double score = 0.0;

        String normalizedLeft = MethodSignature.normalizeSignature(left.ref().signature());
        String normalizedRight = MethodSignature.normalizeSignature(right.ref().signature());
        if (normalizedLeft.equals(normalizedRight)) {
            score += 0.55;
        }
        if (left.ref().methodName().equals(right.ref().methodName())) {
            score += 0.2;
        }
        if (left.ref().arity() == right.ref().arity()) {
            score += 0.1;
        }
        if (left.ref().relativePath().equals(right.ref().relativePath())) {
            score += 0.15;
        }

        score += 0.25 * JavaModelBuilder.bodySimilarity(left, right);
        return Math.min(score, 1.0);
    }

    private static boolean methodMatchesPath(MethodRef ref, String normalizedFileInput, String absoluteFileInput) {
        String methodRelative = normalizePath(ref.relativePath());
        String methodAbsolute = normalizePath(ref.absolutePath());

        if (absoluteFileInput != null && methodAbsolute.equals(normalizePath(absoluteFileInput))) {
            return true;
        }
        if (methodRelative.equals(normalizedFileInput) || methodAbsolute.equals(normalizedFileInput)) {
            return true;
        }

        String fileName = Path.of(normalizedFileInput).getFileName().toString().toLowerCase(Locale.ROOT);
        return methodRelative.toLowerCase(Locale.ROOT).endsWith(fileName)
                || methodAbsolute.toLowerCase(Locale.ROOT).endsWith(fileName);
    }

    static String normalizePath(String path) {
        return path.replace('\\', '/');
    }

    static String paramsToSignature(String methodName, String paramsRaw) {
        if (paramsRaw == null || paramsRaw.isBlank()) {
            return methodName + "()";
        }
        String p = paramsRaw.trim();
        if (!p.startsWith("(")) {
            p = "(" + p + ")";
        }
        return methodName + p;
    }

    private record Candidate(MethodModel left, MethodModel right, double score) {
    }
}
