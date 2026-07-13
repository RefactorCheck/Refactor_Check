package com.refactorcheck.core.model;

import java.util.List;
import java.util.Objects;

public final class MethodRef {
    private final String methodKey;
    private final String relativePath;
    private final String absolutePath;
    private final String container;
    private final String methodName;
    private final List<String> parameterTypes;
    private final String signature;
    private final int arity;
    private final int startLine;
    private final int endLine;

    public MethodRef(
            String methodKey,
            String relativePath,
            String absolutePath,
            String container,
            String methodName,
            List<String> parameterTypes,
            String signature,
            int arity,
            int startLine,
            int endLine) {
        this.methodKey = methodKey;
        this.relativePath = relativePath;
        this.absolutePath = absolutePath;
        this.container = container;
        this.methodName = methodName;
        this.parameterTypes = List.copyOf(parameterTypes);
        this.signature = signature;
        this.arity = arity;
        this.startLine = startLine;
        this.endLine = endLine;
    }

    public String methodKey() {
        return methodKey;
    }

    public String relativePath() {
        return relativePath;
    }

    public String absolutePath() {
        return absolutePath;
    }

    public String container() {
        return container;
    }

    public String methodName() {
        return methodName;
    }

    public List<String> parameterTypes() {
        return parameterTypes;
    }

    public String signature() {
        return signature;
    }

    public int arity() {
        return arity;
    }

    public int startLine() {
        return startLine;
    }

    public int endLine() {
        return endLine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MethodRef methodRef)) {
            return false;
        }
        return Objects.equals(methodKey, methodRef.methodKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(methodKey);
    }

    @Override
    public String toString() {
        return relativePath + "::" + container + "::" + signature;
    }
}
