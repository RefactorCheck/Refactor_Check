package com.refactorcheck.core.model;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public final class MethodSignature {
    private MethodSignature() {
    }

    public static String build(String methodName, List<String> parameterTypes) {
        return methodName + "(" + parameterTypes.stream().collect(Collectors.joining(",")) + ")";
    }

    public static String normalizeType(String type) {
        if (type == null) {
            return "";
        }
        String cleaned = type.replace(" ", "").replace("?extends", "").replace("?super", "");
        if (cleaned.endsWith("...")) {
            cleaned = cleaned.substring(0, cleaned.length() - 3) + "[]";
        }
        return cleaned;
    }

    public static String simplifyType(String type) {
        String normalized = normalizeType(type);
        int genericStart = normalized.indexOf('<');
        String raw = genericStart >= 0 ? normalized.substring(0, genericStart) : normalized;
        int dot = raw.lastIndexOf('.');
        return (dot >= 0 ? raw.substring(dot + 1) : raw).toLowerCase(Locale.ROOT);
    }

    public static String normalizeSignature(String signature) {
        String compact = signature.replace(" ", "");
        int left = compact.indexOf('(');
        int right = compact.lastIndexOf(')');
        if (left < 0 || right <= left) {
            return compact;
        }
        String name = compact.substring(0, left);
        String params = compact.substring(left + 1, right);
        if (params.isBlank()) {
            return name + "()";
        }
        String normalizedParams = List.of(params.split(","))
                .stream()
                .map(MethodSignature::simplifyType)
                .collect(Collectors.joining(","));
        return name + "(" + normalizedParams + ")";
    }
}
