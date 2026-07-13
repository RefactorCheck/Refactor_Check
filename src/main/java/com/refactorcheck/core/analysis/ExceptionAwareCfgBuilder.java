package com.refactorcheck.core.analysis;

import com.refactorcheck.core.input.SourceSnapshot;
import com.refactorcheck.core.model.CallSite;
import com.refactorcheck.core.model.CfgNode;
import com.refactorcheck.core.model.MethodCfg;
import com.refactorcheck.core.model.MethodModel;
import com.refactorcheck.core.model.MethodRef;
import com.refactorcheck.core.model.StatementModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public final class ExceptionAwareCfgBuilder implements CfgBuilder {

    @Override
    public MethodCfg build(SourceSnapshot snapshot, MethodRef method) {
        MethodModel root = snapshot.methodsByKey().get(method.methodKey());
        if (root == null) {
            throw new IllegalArgumentException("Method not found in snapshot: " + method.methodKey());
        }

        List<CfgNode> expanded = new ArrayList<>();
        expand(snapshot, root, new HashSet<>(), expanded);
        Map<Integer, Set<Integer>> edges = buildDependencyEdges(expanded);

        return new MethodCfg(method, expanded, edges);
    }

    private void expand(SourceSnapshot snapshot, MethodModel current, Set<String> callStack, List<CfgNode> out) {
        if (callStack.contains(current.ref().methodKey())) {
            return;
        }
        callStack.add(current.ref().methodKey());

        for (StatementModel statement : current.statements()) {
            Optional<MethodModel> substitution = standaloneInternalCallee(snapshot, current, statement);
            if (substitution.isPresent()
                    && !callStack.contains(substitution.get().ref().methodKey())) {
                expand(snapshot, substitution.get(), new HashSet<>(callStack), out);
                continue;
            }

            int nodeIndex = out.size();
            out.add(toCfgNode(nodeIndex, statement));
        }
    }

    private Optional<MethodModel> standaloneInternalCallee(
            SourceSnapshot snapshot,
            MethodModel caller,
            StatementModel statement) {
        if (statement.kind() != com.refactorcheck.core.model.NodeKind.METHOD_CALL
                || statement.externalCall()
                || statement.callSites().size() != 1) {
            return Optional.empty();
        }
        CallSite callSite = statement.callSites().iterator().next();
        return resolveCallee(snapshot, caller, callSite);
    }

    private Optional<MethodModel> resolveCallee(SourceSnapshot snapshot, MethodModel caller, CallSite callSite) {
        List<MethodModel> candidates = snapshot.findMethodsByNameAndArity(callSite.methodName(), callSite.arity());
        if (candidates.isEmpty()) {
            return Optional.empty();
        }

        
        Optional<MethodModel> sameContainer = candidates.stream()
                .filter(candidate -> candidate.ref().container().equals(caller.ref().container()))
                .findFirst();
        return sameContainer.isPresent() ? sameContainer : Optional.of(candidates.get(0));
    }

    private CfgNode toCfgNode(int index, StatementModel statement) {
        return new CfgNode(
                index,
                statement.line(),
                statement.normalizedText(),
                statement.kind(),
                statement.usedIdentifiers(),
                statement.definedIdentifiers(),
                statement.literals(),
                statement.callSites(),
                statement.exceptionKinds(),
                statement.behaviorChanging(),
                statement.controlPoint(),
                statement.ioOperation(),
                statement.externalCall());
    }

    private Map<Integer, Set<Integer>> buildDependencyEdges(List<CfgNode> nodes) {
        Map<Integer, Set<Integer>> edges = new HashMap<>();
        Map<String, Integer> lastDefinition = new HashMap<>();

        for (int i = 0; i < nodes.size(); i++) {
            CfgNode node = nodes.get(i);
            edges.putIfAbsent(i, new HashSet<>());
            if (i + 1 < nodes.size()) {
                edges.get(i).add(i + 1);
            }

            for (String used : node.usedIdentifiers()) {
                Integer def = lastDefinition.get(used);
                if (def != null && def < i) {
                    edges.computeIfAbsent(def, unused -> new HashSet<>()).add(i);
                }
            }
            for (String defined : node.definedIdentifiers()) {
                lastDefinition.put(defined, i);
            }
        }

        return edges;
    }
}
