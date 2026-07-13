package com.refactorcheck.core.impact;

import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.jgit.lib.Repository;
import org.remapper.dto.CodeRange;
import org.remapper.dto.EntityInfo;
import org.remapper.dto.EntityType;
import org.remapper.dto.MatchPair;
import org.remapper.service.GitService;
import org.remapper.util.GitServiceImpl;
import org.reextractor.handler.RefactoringHandler;
import org.reextractor.refactoring.Refactoring;
import org.reextractor.service.RefactoringExtractorService;
import org.reextractor.service.RefactoringExtractorServiceImpl;

import com.refactorcheck.core.input.SourceFileUnit;
import com.refactorcheck.core.input.SourceSnapshot;
import com.refactorcheck.core.model.ImpactSource;
import com.refactorcheck.core.model.MethodModel;
import com.refactorcheck.core.model.MethodPair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ReExtractorImpactAnalyzer implements ImpactAnalyzer {
    private static final Pattern METHOD_NAME_PATTERN = Pattern.compile("([A-Za-z_$][A-Za-z0-9_$]*)\\s*\\(");

    @Override
    public ImpactAnalysisResult analyze(SourceSnapshot before, SourceSnapshot after) {
        Path tempRepo = null;
        try {
            tempRepo = Files.createTempDirectory("refactorcheck-reextractor-");
            String commitId = prepareRepo(tempRepo, before, after);

            AtomicReference<MatchPair> matchPairRef = new AtomicReference<>();
            AtomicReference<List<Refactoring>> refactoringsRef = new AtomicReference<>(List.of());

            String originalUserHome = System.getProperty("user.home");
            try {
                
                System.setProperty("user.home", tempRepo.toString());
                GitService gitService = new GitServiceImpl();
                try (Repository repository = gitService.openRepository(tempRepo.toString())) {
                    RefactoringExtractorService service = new RefactoringExtractorServiceImpl();
                    service.detectAtCommit(repository, commitId, new RefactoringHandler() {
                        @Override
                        public void handle(String commitId, MatchPair matchPair, List<Refactoring> refactorings) {
                            matchPairRef.set(matchPair);
                            refactoringsRef.set(refactorings == null ? List.of() : List.copyOf(refactorings));
                        }

                        @Override
                        public void handleException(String commitId, Exception e) {
                            throw new ImpactAnalysisException("ReExtractor failed at commit " + commitId, e);
                        }
                    });
                }
            } finally {
                if (originalUserHome != null) {
                    System.setProperty("user.home", originalUserHome);
                }
            }

            MatchPair matchPair = matchPairRef.get();
            if (matchPair == null) {
                throw new ImpactAnalysisException("ReExtractor returned no match data.");
            }

            List<MethodPair> seedPairs = extractMethodPairs(before, after, matchPair, refactoringsRef.get());
            if (seedPairs.isEmpty()) {
                throw new ImpactAnalysisException("ReExtractor did not produce any method pairs.");
            }

            List<MethodPair> expanded = ImpactUtils.expandWithCallers(before, after, seedPairs, ImpactSource.CALLER_EXPANSION);
            return new ImpactAnalysisResult(expanded, "REEXTRACTOR", false, List.of());
        } catch (IOException e) {
            throw new ImpactAnalysisException("Failed to prepare temporary git repository for ReExtractor", e);
        } finally {
            if (tempRepo != null) {
                cleanupTempRepo(tempRepo);
            }
        }
    }

    private String prepareRepo(Path tempRepo, SourceSnapshot before, SourceSnapshot after) throws IOException {
        runGit(tempRepo, "git", "init");
        runGit(tempRepo, "git", "config", "user.email", "refactorcheck@local");
        runGit(tempRepo, "git", "config", "user.name", "refactorcheck");

        writeSnapshot(tempRepo, before);
        runGit(tempRepo, "git", "add", "-A");
        runGit(tempRepo, "git", "commit", "-m", "before");

        clearWorktree(tempRepo);

        writeSnapshot(tempRepo, after);
        runGit(tempRepo, "git", "add", "-A");
        runGit(tempRepo, "git", "commit", "--allow-empty", "-m", "after");

        return runGit(tempRepo, "git", "rev-parse", "HEAD").trim();
    }

    private void writeSnapshot(Path repoRoot, SourceSnapshot snapshot) throws IOException {
        for (SourceFileUnit unit : snapshot.files()) {
            Path target = repoRoot.resolve(unit.relativePath()).normalize();
            if (!target.startsWith(repoRoot)) {
                throw new IOException("Unsafe relative path in snapshot: " + unit.relativePath());
            }
            Files.createDirectories(target.getParent());
            Files.writeString(target, unit.content(), StandardCharsets.UTF_8);
        }
    }

    private void clearWorktree(Path repoRoot) throws IOException {
        Path gitDir = repoRoot.resolve(".git").normalize();
        try (var stream = Files.walk(repoRoot)) {
            stream.sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        if (path.equals(repoRoot)) {
                            return;
                        }
                        Path normalized = path.normalize();
                        if (normalized.equals(gitDir) || normalized.startsWith(gitDir)) {
                            return;
                        }
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        } catch (RuntimeException ex) {
            if (ex.getCause() instanceof IOException ioException) {
                throw ioException;
            }
            throw ex;
        }
    }

    private String runGit(Path repoRoot, String... command) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.directory(repoRoot.toFile());
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append(System.lineSeparator());
            }
        }

        try {
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IOException("Git command failed (" + String.join(" ", command) + "): " + output);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Git command interrupted: " + String.join(" ", command), e);
        }

        return output.toString();
    }

    private List<MethodPair> extractMethodPairs(
            SourceSnapshot before,
            SourceSnapshot after,
            MatchPair matchPair,
            List<Refactoring> refactorings) {
        Map<String, MethodPair> unique = new LinkedHashMap<>();

        addEntityPairs(unique, before, after, matchPair.getUnchangedEntityInfos(), 0.9);
        addEntityPairs(unique, before, after, matchPair.getMatchedEntityInfos(), 0.88);
        addEntityPairs(unique, before, after, matchPair.getCandidateEntityInfos(), 0.75);

        Map<String, MethodRefPair> indexed = indexByBefore(unique.values());
        addRefactoringImpacts(unique, indexed, before, after, refactorings);

        return new ArrayList<>(unique.values());
    }

    private void addEntityPairs(
            Map<String, MethodPair> unique,
            SourceSnapshot before,
            SourceSnapshot after,
            Set<Pair<EntityInfo, EntityInfo>> entityPairs,
            double confidence) {
        for (Pair<EntityInfo, EntityInfo> pair : entityPairs) {
            EntityInfo left = pair.getLeft();
            EntityInfo right = pair.getRight();
            if (left.getType() != EntityType.METHOD || right.getType() != EntityType.METHOD) {
                continue;
            }

            int leftArity = parseArity(left.getParams());
            int rightArity = parseArity(right.getParams());

            Optional<MethodModel> leftMethod = ImpactUtils.resolveByLocation(
                    before,
                    left.getLocationInfo().getFilePath(),
                    left.getLocationInfo().getStartLine(),
                    left.getLocationInfo().getEndLine(),
                    left.getName(),
                    leftArity);

            Optional<MethodModel> rightMethod = ImpactUtils.resolveByLocation(
                    after,
                    right.getLocationInfo().getFilePath(),
                    right.getLocationInfo().getStartLine(),
                    right.getLocationInfo().getEndLine(),
                    right.getName(),
                    rightArity);

            if (leftMethod.isEmpty() || rightMethod.isEmpty()) {
                continue;
            }

            MethodPair methodPair = new MethodPair(
                    leftMethod.get().ref(),
                    rightMethod.get().ref(),
                    ImpactSource.REEXTRACTOR,
                    confidence);
            unique.putIfAbsent(keyOf(methodPair), methodPair);
        }
    }

    private void addRefactoringImpacts(
            Map<String, MethodPair> unique,
            Map<String, MethodRefPair> indexed,
            SourceSnapshot before,
            SourceSnapshot after,
            List<Refactoring> refactorings) {
        for (Refactoring refactoring : refactorings) {
            Set<MethodModel> leftMethods = new LinkedHashSet<>();
            Set<MethodModel> rightMethods = new LinkedHashSet<>();

            for (CodeRange range : refactoring.leftSide()) {
                String methodName = parseMethodName(range.getCodeElement());
                int arity = parseArityFromCodeElement(range.getCodeElement());
                ImpactUtils.resolveByLocation(
                        before,
                        range.getFilePath(),
                        range.getStartLine(),
                        range.getEndLine(),
                        methodName,
                        arity).ifPresent(leftMethods::add);
            }
            for (CodeRange range : refactoring.rightSide()) {
                String methodName = parseMethodName(range.getCodeElement());
                int arity = parseArityFromCodeElement(range.getCodeElement());
                ImpactUtils.resolveByLocation(
                        after,
                        range.getFilePath(),
                        range.getStartLine(),
                        range.getEndLine(),
                        methodName,
                        arity).ifPresent(rightMethods::add);
            }

            if (leftMethods.isEmpty() || rightMethods.isEmpty()) {
                continue;
            }

            for (MethodModel left : leftMethods) {
                MethodRefPair indexedPair = indexed.get(left.ref().methodKey());
                MethodModel mappedRight = null;
                if (indexedPair != null) {
                    mappedRight = after.methodsByKey().containsKey(indexedPair.after().methodKey())
                            ? after.methodsByKey().get(indexedPair.after().methodKey())
                            : null;
                }

                if (mappedRight == null) {
                    mappedRight = rightMethods.stream()
                            .filter(r -> r.ref().methodName().equals(left.ref().methodName()))
                            .filter(r -> r.ref().arity() == left.ref().arity())
                            .findFirst()
                            .orElse(rightMethods.iterator().next());
                }

                MethodPair pair = new MethodPair(left.ref(), mappedRight.ref(), ImpactSource.REEXTRACTOR, 0.72);
                unique.putIfAbsent(keyOf(pair), pair);
            }
        }
    }

    private Map<String, MethodRefPair> indexByBefore(Collection<MethodPair> pairs) {
        Map<String, MethodRefPair> map = new HashMap<>();
        for (MethodPair pair : pairs) {
            map.put(pair.before().methodKey(), new MethodRefPair(pair.before(), pair.after()));
        }
        return map;
    }

    private String parseMethodName(String codeElement) {
        if (codeElement == null) {
            return null;
        }
        Matcher matcher = METHOD_NAME_PATTERN.matcher(codeElement);
        String name = null;
        while (matcher.find()) {
            name = matcher.group(1);
        }
        return name;
    }

    private int parseArity(String params) {
        if (params == null || params.isBlank()) {
            return 0;
        }
        String trimmed = params.trim();
        if (trimmed.equals("()") || trimmed.equals("")) {
            return 0;
        }
        String body = trimmed;
        if (body.startsWith("(")) {
            body = body.substring(1);
        }
        if (body.endsWith(")")) {
            body = body.substring(0, body.length() - 1);
        }
        if (body.isBlank()) {
            return 0;
        }
        return (int) body.chars().filter(ch -> ch == ',').count() + 1;
    }

    private int parseArityFromCodeElement(String codeElement) {
        if (codeElement == null) {
            return -1;
        }
        int left = codeElement.indexOf('(');
        int right = codeElement.indexOf(')', left + 1);
        if (left < 0 || right < 0 || right <= left + 1) {
            return 0;
        }
        String args = codeElement.substring(left + 1, right).trim();
        if (args.isBlank()) {
            return 0;
        }
        return (int) args.chars().filter(ch -> ch == ',').count() + 1;
    }

    private String keyOf(MethodPair pair) {
        return pair.before().methodKey() + "->" + pair.after().methodKey();
    }

    private void cleanupTempRepo(Path tempRepo) {
        try (var stream = Files.walk(tempRepo)) {
            stream.sorted(Comparator.reverseOrder()).forEach(path -> {
                try {
                    Files.deleteIfExists(path);
                } catch (IOException ignored) {
                }
            });
        } catch (IOException ignored) {
        }
    }

    private record MethodRefPair(com.refactorcheck.core.model.MethodRef before, com.refactorcheck.core.model.MethodRef after) {
    }
}
