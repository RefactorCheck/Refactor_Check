package com.refactorcheck.core.model;

import java.util.List;
import java.util.Set;

public record MethodModel(
        MethodRef ref,
        String bodyText,
        List<StatementModel> statements,
        Set<CallSite> callSites) {
}
