package com.refactorcheck.core.input;

import com.refactorcheck.core.model.MethodModel;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class SourceSnapshot {
    private final String label;
    private final Path root;
    private final List<SourceFileUnit> files;
    private final Map<String, SourceFileUnit> filesByRelativePath;
    private final Map<String, MethodModel> methodsByKey;
    private final Map<String, List<MethodModel>> methodsByNameArity;

    public SourceSnapshot(
            String label,
            Path root,
            List<SourceFileUnit> files,
            Map<String, MethodModel> methodsByKey,
            Map<String, List<MethodModel>> methodsByNameArity) {
        this.label = label;
        this.root = root;
        this.files = List.copyOf(files);
        this.filesByRelativePath = this.files.stream()
                .collect(Collectors.toUnmodifiableMap(SourceFileUnit::relativePath, f -> f, (a, b) -> a));
        this.methodsByKey = Map.copyOf(methodsByKey);
        this.methodsByNameArity = Map.copyOf(methodsByNameArity);
    }

    public String label() {
        return label;
    }

    public Path root() {
        return root;
    }

    public List<SourceFileUnit> files() {
        return files;
    }

    public Map<String, SourceFileUnit> filesByRelativePath() {
        return filesByRelativePath;
    }

    public Map<String, MethodModel> methodsByKey() {
        return methodsByKey;
    }

    public Collection<MethodModel> methods() {
        return methodsByKey.values();
    }

    public List<MethodModel> findMethodsByNameAndArity(String methodName, int arity) {
        return methodsByNameArity.getOrDefault(methodName + "#" + arity, List.of());
    }
}
