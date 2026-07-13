package com.refactorcheck.core.model;

import java.util.Set;

public record CfgNode(
        int index,
        int line,
        String text,
        NodeKind kind,
        Set<String> usedIdentifiers,
        Set<String> definedIdentifiers,
        Set<String> literals,
        Set<CallSite> callSites,
        Set<ExceptionKind> exceptionKinds,
        boolean behaviorChanging,
        boolean controlPoint,
        boolean ioOperation,
        boolean externalCall) {
}
