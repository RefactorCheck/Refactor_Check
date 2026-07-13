package com.refactorcheck.core.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

public record MethodCfg(
        MethodRef method,
        List<CfgNode> nodes,
        Map<Integer, Set<Integer>> dependencyEdges) {
}
