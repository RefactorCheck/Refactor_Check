package com.refactorcheck.core.analysis;

import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.refactorcheck.core.input.SourceFileUnit;
import com.refactorcheck.core.input.SourceSnapshot;
import com.refactorcheck.core.model.CallSite;
import com.refactorcheck.core.model.MethodModel;
import com.refactorcheck.core.model.MethodRef;
import com.refactorcheck.core.model.MethodSignature;
import com.refactorcheck.core.model.StatementModel;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public final class JavaModelBuilder {

    public SourceSnapshot buildSnapshot(String label, Path root, List<SourceFileUnit> files) {
        Map<String, MethodModel> methodsByKey = new HashMap<>();
        Map<String, List<MethodModel>> methodsByNameArity = new HashMap<>();

        for (SourceFileUnit file : files) {
            parseFile(file, methodsByKey, methodsByNameArity);
        }

        return new SourceSnapshot(label, root, files, methodsByKey, methodsByNameArity);
    }

    private void parseFile(
            SourceFileUnit file,
            Map<String, MethodModel> methodsByKey,
            Map<String, List<MethodModel>> methodsByNameArity) {
        Optional<CompilationUnit> parsed = parseCompilationUnitWithRecovery(file.content());
        if (parsed.isEmpty()) {
            return;
        }
        CompilationUnit cu = parsed.get();

        String packageName = cu.getPackageDeclaration().map(pd -> pd.getNameAsString()).orElse("");

        for (MethodDeclaration method : cu.findAll(MethodDeclaration.class)) {
            toModel(file, packageName, method).ifPresent(model -> addModel(model, methodsByKey, methodsByNameArity));
        }
        for (ConstructorDeclaration constructor : cu.findAll(ConstructorDeclaration.class)) {
            toModel(file, packageName, constructor).ifPresent(model -> addModel(model, methodsByKey, methodsByNameArity));
        }
    }

    private Optional<CompilationUnit> parseCompilationUnitWithRecovery(String content) {
        Optional<CompilationUnit> direct = parseCompilationUnit(content);
        if (direct.isPresent()) {
            return direct;
        }

        String withoutPackages = stripPackageDeclarations(content);
        Optional<CompilationUnit> packageRecovered = parseCompilationUnit(withoutPackages);
        if (packageRecovered.isPresent()) {
            return packageRecovered;
        }

        String wrapped = "class __RefactorCheckWrapper {\n"
                + stripImportDeclarations(withoutPackages)
                + "\n}";
        return parseCompilationUnit(wrapped);
    }

    private Optional<CompilationUnit> parseCompilationUnit(String content) {
        try {
            return Optional.of(StaticJavaParser.parse(content));
        } catch (ParseProblemException ex) {
            return Optional.empty();
        }
    }

    private String stripPackageDeclarations(String content) {
        return content.replaceAll("(?m)^\\s*package\\s+[A-Za-z0-9_$.]+\\s*;\\s*$", "");
    }

    private String stripImportDeclarations(String content) {
        return content.replaceAll("(?m)^\\s*import\\s+[A-Za-z0-9_$.]+\\s*;\\s*$", "");
    }

    private Optional<MethodModel> toModel(SourceFileUnit file, String packageName, CallableDeclaration<?> callable) {
        Optional<BlockStmt> body = body(callable);
        if (body.isEmpty()) {
            return Optional.empty();
        }

        List<String> parameterTypes = callable.getParameters().stream()
                .map(Parameter::getType)
                .map(type -> MethodSignature.normalizeType(type.asString()))
                .toList();

        String methodName = callable.getNameAsString();
        String signature = MethodSignature.build(methodName, parameterTypes);

        int startLine = callable.getRange().map(r -> r.begin.line).orElse(-1);
        int endLine = callable.getRange().map(r -> r.end.line).orElse(-1);

        String container = resolveContainerName(callable, packageName);
        String methodKey = file.relativePath() + "::" + container + "::" + signature + "::" + startLine;

        List<StatementModel> statements = StatementAnalyzer.extract(body.get());
        Set<CallSite> callSites = new HashSet<>();
        for (StatementModel statement : statements) {
            callSites.addAll(statement.callSites());
        }

        MethodRef ref = new MethodRef(
                methodKey,
                file.relativePath(),
                file.absolutePath().toString(),
                container,
                methodName,
                parameterTypes,
                signature,
                parameterTypes.size(),
                startLine,
                endLine);

        MethodModel model = new MethodModel(ref, body.get().toString(), statements, callSites);
        return Optional.of(model);
    }

    private Optional<BlockStmt> body(CallableDeclaration<?> callable) {
        if (callable instanceof MethodDeclaration methodDeclaration) {
            return methodDeclaration.getBody();
        }
        if (callable instanceof ConstructorDeclaration constructorDeclaration) {
            return Optional.of(constructorDeclaration.getBody());
        }
        return Optional.empty();
    }

    private void addModel(
            MethodModel model,
            Map<String, MethodModel> methodsByKey,
            Map<String, List<MethodModel>> methodsByNameArity) {
        methodsByKey.put(model.ref().methodKey(), model);
        String key = model.ref().methodName() + "#" + model.ref().arity();
        methodsByNameArity.computeIfAbsent(key, unused -> new ArrayList<>()).add(model);
    }

    private String resolveContainerName(Node callable, String packageName) {
        List<String> owners = new ArrayList<>();
        Node current = callable;
        while (current != null) {
            if (current instanceof TypeDeclaration<?> typeDeclaration) {
                owners.add(typeDeclaration.getNameAsString());
            }
            current = current.getParentNode().orElse(null);
        }

        String ownerPart = owners.isEmpty()
                ? "<anonymous>"
                : reverseJoin(owners);

        if (packageName == null || packageName.isBlank()) {
            return ownerPart;
        }
        return packageName + "." + ownerPart;
    }

    public static double bodySimilarity(MethodModel left, MethodModel right) {
        Set<String> leftTokens = tokenize(left.bodyText());
        Set<String> rightTokens = tokenize(right.bodyText());
        if (leftTokens.isEmpty() && rightTokens.isEmpty()) {
            return 1.0;
        }
        Set<String> intersection = new HashSet<>(leftTokens);
        intersection.retainAll(rightTokens);
        Set<String> union = new HashSet<>(leftTokens);
        union.addAll(rightTokens);
        if (union.isEmpty()) {
            return 0.0;
        }
        return (double) intersection.size() / (double) union.size();
    }

    private static Set<String> tokenize(String text) {
        String[] split = text.toLowerCase(Locale.ROOT).split("[^a-z0-9_]+");
        Set<String> tokens = new HashSet<>();
        for (String token : split) {
            if (!token.isBlank()) {
                tokens.add(token);
            }
        }
        return tokens;
    }

    private static String reverseJoin(List<String> values) {
        if (values.isEmpty()) {
            return "<anonymous>";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = values.size() - 1; i >= 0; i--) {
            if (builder.length() > 0) {
                builder.append('.');
            }
            builder.append(values.get(i));
        }
        return builder.toString();
    }
}
