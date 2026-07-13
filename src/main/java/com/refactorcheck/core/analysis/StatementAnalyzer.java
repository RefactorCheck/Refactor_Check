package com.refactorcheck.core.analysis;

import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.InstanceOfExpr;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.LongLiteralExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.stmt.ThrowStmt;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.refactorcheck.core.model.CallSite;
import com.refactorcheck.core.model.ExceptionKind;
import com.refactorcheck.core.model.NodeKind;
import com.refactorcheck.core.model.StatementModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

final class StatementAnalyzer {
    private StatementAnalyzer() {
    }

    static List<StatementModel> extract(BlockStmt body) {
        List<Statement> statements = body.findAll(Statement.class).stream()
                .filter(stmt -> !(stmt instanceof BlockStmt))
                .sorted(Comparator
                        .comparingInt((Statement s) -> s.getRange().map(r -> r.begin.line).orElse(Integer.MAX_VALUE))
                        .thenComparingInt(s -> s.getRange().map(r -> r.begin.column).orElse(Integer.MAX_VALUE)))
                .toList();

        List<StatementModel> models = new ArrayList<>();
        int index = 0;
        for (Statement statement : statements) {
            models.add(toModel(index++, statement));
        }
        return models;
    }

    private static StatementModel toModel(int index, Statement statement) {
        String raw = statement.toString();
        String normalized = normalize(raw);
        int line = statement.getRange().map(r -> r.begin.line).orElse(-1);

        NodeKind kind = detectKind(statement);
        Set<String> usedIdentifiers = statement.findAll(NameExpr.class).stream()
                .map(NameExpr::getNameAsString)
                .collect(Collectors.toCollection(HashSet::new));
        Set<String> definedIdentifiers = detectDefinedIdentifiers(statement);
        Set<String> literals = detectLiterals(statement);
        Set<CallSite> callSites = statement.findAll(MethodCallExpr.class).stream()
                .map(call -> new CallSite(
                        call.getNameAsString(),
                        call.getArguments().size(),
                        call.getRange().map(r -> r.begin.line).orElse(line)))
                .collect(Collectors.toCollection(HashSet::new));
        Set<ExceptionKind> exceptionKinds = detectExceptionKinds(statement);

        boolean controlPoint = isControlPoint(statement);
        boolean ioOperation = hasIOPattern(statement);
        boolean externalCall = statement.findAll(MethodCallExpr.class).stream()
                .anyMatch(call -> call.getScope().isPresent()
                        && !(call.getScope().get().isThisExpr() || call.getScope().get().isSuperExpr()));

        boolean behaviorChanging = controlPoint
                || ioOperation
                || externalCall
                || kind == NodeKind.ASSIGNMENT
                || kind == NodeKind.VARIABLE_DECLARATION
                || kind == NodeKind.RETURN
                || kind == NodeKind.THROW
                || kind == NodeKind.METHOD_CALL;

        return new StatementModel(
                index,
                line,
                raw,
                normalized,
                kind,
                usedIdentifiers,
                definedIdentifiers,
                literals,
                callSites,
                exceptionKinds,
                behaviorChanging,
                controlPoint,
                ioOperation,
                externalCall);
    }

    private static NodeKind detectKind(Statement statement) {
        if (statement instanceof IfStmt) {
            return NodeKind.IF;
        }
        if (statement instanceof ForStmt || statement instanceof ForEachStmt || statement instanceof WhileStmt || statement instanceof DoStmt) {
            return NodeKind.LOOP;
        }
        if (statement instanceof SwitchStmt) {
            return NodeKind.SWITCH;
        }
        if (statement instanceof ReturnStmt) {
            return NodeKind.RETURN;
        }
        if (statement instanceof ThrowStmt) {
            return NodeKind.THROW;
        }
        if (statement instanceof ExpressionStmt expressionStmt) {
            Expression expression = expressionStmt.getExpression();
            if (expression instanceof AssignExpr) {
                return NodeKind.ASSIGNMENT;
            }
            if (expression instanceof VariableDeclarationExpr) {
                return NodeKind.VARIABLE_DECLARATION;
            }
            if (expression instanceof MethodCallExpr) {
                return NodeKind.METHOD_CALL;
            }
        }
        if (statement.isTryStmt()) {
            return NodeKind.CONTROL;
        }
        return NodeKind.OTHER;
    }

    private static Set<String> detectDefinedIdentifiers(Statement statement) {
        Set<String> identifiers = new HashSet<>();
        statement.findAll(VariableDeclarationExpr.class)
                .forEach(expr -> expr.getVariables().forEach(var -> identifiers.add(var.getNameAsString())));
        statement.findAll(AssignExpr.class)
                .forEach(assign -> identifiers.add(assign.getTarget().toString()));
        statement.findAll(UnaryExpr.class)
                .stream()
                .filter(unary -> unary.getOperator() == UnaryExpr.Operator.POSTFIX_DECREMENT
                        || unary.getOperator() == UnaryExpr.Operator.POSTFIX_INCREMENT
                        || unary.getOperator() == UnaryExpr.Operator.PREFIX_DECREMENT
                        || unary.getOperator() == UnaryExpr.Operator.PREFIX_INCREMENT)
                .forEach(unary -> identifiers.add(unary.getExpression().toString()));
        return identifiers;
    }

    private static Set<String> detectLiterals(Statement statement) {
        Set<String> literals = new HashSet<>();
        statement.findAll(StringLiteralExpr.class).forEach(l -> literals.add('"' + l.getValue() + '"'));
        statement.findAll(IntegerLiteralExpr.class).forEach(l -> literals.add(l.getValue()));
        statement.findAll(LongLiteralExpr.class).forEach(l -> literals.add(l.getValue()));
        statement.findAll(NullLiteralExpr.class).forEach(l -> literals.add("null"));
        return literals;
    }

    private static Set<ExceptionKind> detectExceptionKinds(Statement statement) {
        Set<ExceptionKind> kinds = new HashSet<>();

        if (!statement.findAll(com.github.javaparser.ast.expr.ArrayAccessExpr.class).isEmpty()
                || statement.findAll(MethodCallExpr.class).stream()
                .anyMatch(StatementAnalyzer::isIndexAccessCall)) {
            kinds.add(ExceptionKind.INDEX_OUT_OF_BOUNDS);
        }

        boolean hasScopeAccess = !statement.findAll(FieldAccessExpr.class).stream()
                .filter(access -> !(access.getScope().isThisExpr() || access.getScope().isSuperExpr()))
                .toList()
                .isEmpty()
                || statement.findAll(MethodCallExpr.class).stream()
                .anyMatch(call -> call.getScope().isPresent()
                        && !(call.getScope().get().isThisExpr() || call.getScope().get().isSuperExpr()));
        String text = statement.toString();
        if (hasScopeAccess && !text.contains("!= null") && !text.contains("== null")) {
            kinds.add(ExceptionKind.NULL_POINTER);
        }

        boolean hasDivision = statement.findAll(BinaryExpr.class).stream()
                .anyMatch(expr -> expr.getOperator() == BinaryExpr.Operator.DIVIDE
                        || expr.getOperator() == BinaryExpr.Operator.REMAINDER);
        if (hasDivision) {
            kinds.add(ExceptionKind.ARITHMETIC);
        }

        boolean hasCast = !statement.findAll(CastExpr.class).isEmpty();
        boolean hasInstanceOf = !statement.findAll(InstanceOfExpr.class).isEmpty();
        if (hasCast && !hasInstanceOf) {
            kinds.add(ExceptionKind.CLASS_CAST);
        }

        if (statement.findAll(MethodCallExpr.class).stream().anyMatch(StatementAnalyzer::isNumberParsingCall)) {
            kinds.add(ExceptionKind.NUMBER_FORMAT);
        }

        if (hasExplicitException(statement, "IllegalStateException")
                || statement.findAll(MethodCallExpr.class).stream().anyMatch(StatementAnalyzer::isIllegalStateCheck)) {
            kinds.add(ExceptionKind.ILLEGAL_STATE);
        }

        if (hasExplicitException(statement, "IllegalArgumentException")
                || statement.findAll(MethodCallExpr.class).stream().anyMatch(StatementAnalyzer::isIllegalArgumentCheck)) {
            kinds.add(ExceptionKind.ILLEGAL_ARGUMENT);
        }

        return kinds;
    }

    private static boolean isIndexAccessCall(MethodCallExpr call) {
        String name = call.getNameAsString();
        int arity = call.getArguments().size();
        return (name.equals("get") && arity == 1)
                || (name.equals("charAt") && arity == 1)
                || (name.equals("codePointAt") && arity == 1)
                || (name.equals("codePointBefore") && arity == 1)
                || (name.equals("offsetByCodePoints") && arity == 2)
                || (name.equals("substring") && (arity == 1 || arity == 2))
                || (name.equals("subSequence") && arity == 2);
    }

    private static boolean isNumberParsingCall(MethodCallExpr call) {
        String name = call.getNameAsString();
        if (!(name.startsWith("parse") || name.equals("valueOf"))) {
            return false;
        }
        if (call.getArguments().isEmpty()) {
            return false;
        }
        String scope = call.getScope().map(Expression::toString).map(String::toLowerCase).orElse("");
        return scope.equals("integer")
                || scope.equals("long")
                || scope.equals("double")
                || scope.equals("float")
                || scope.equals("short")
                || scope.equals("byte")
                || scope.equals("biginteger")
                || scope.equals("bigdecimal");
    }

    private static boolean isIllegalStateCheck(MethodCallExpr call) {
        String name = call.getNameAsString();
        String scope = call.getScope().map(Expression::toString).map(String::toLowerCase).orElse("");
        return name.equals("checkState")
                || (name.equals("state") && (scope.endsWith("assert") || scope.endsWith("assertions")));
    }

    private static boolean isIllegalArgumentCheck(MethodCallExpr call) {
        String name = call.getNameAsString();
        String scope = call.getScope().map(Expression::toString).map(String::toLowerCase).orElse("");
        return name.equals("checkArgument")
                || (name.equals("isTrue") && (scope.endsWith("assert") || scope.endsWith("assertions")));
    }

    private static boolean hasExplicitException(Statement statement, String exceptionName) {
        return statement.findAll(ObjectCreationExpr.class).stream()
                .map(created -> created.getType().getNameAsString())
                .anyMatch(exceptionName::equals);
    }

    private static boolean isControlPoint(Statement statement) {
        return statement instanceof IfStmt
                || statement instanceof ForStmt
                || statement instanceof ForEachStmt
                || statement instanceof WhileStmt
                || statement instanceof DoStmt
                || statement instanceof SwitchStmt
                || statement instanceof TryStmt;
    }

    private static boolean hasIOPattern(Statement statement) {
        String raw = statement.toString().toLowerCase(Locale.ROOT);
        if (raw.contains("system.out") || raw.contains("system.err")) {
            return true;
        }
        return statement.findAll(MethodCallExpr.class).stream()
                .map(call -> call.getNameAsString().toLowerCase(Locale.ROOT))
                .anyMatch(name -> name.contains("print")
                        || name.contains("read")
                        || name.contains("write")
                        || name.contains("send")
                        || name.contains("recv")
                        || name.contains("query")
                        || name.contains("execute")
                        || name.contains("save")
                        || name.contains("load"));
    }

    private static String normalize(String text) {
        return text.replaceAll("\\s+", " ").trim();
    }
}
