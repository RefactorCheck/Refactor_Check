package com.refactorcheck.core.check;

import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.refactorcheck.core.input.SourceFileUnit;
import com.refactorcheck.core.input.SourceSnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DeclarationRiskAnalyzer {

    private void collectUnparseableFileRisks(
            SourceSnapshot before,
            SourceSnapshot after,
            Map<String, DeclarationRisk> out) {
        for (SourceFileUnit beforeFile : before.files()) {
            SourceFileUnit afterFile = after.filesByRelativePath().get(beforeFile.relativePath());
            if (afterFile == null) {
                continue;
            }
            if (parseCompilationUnit(beforeFile.content()).isEmpty()) {
                continue;
            }
            if (parseCompilationUnit(afterFile.content()).isPresent()) {
                continue;
            }
            if (looksLikeRecordPromotionSnippet(beforeFile.content(), afterFile.content())) {
                continue;
            }

            String key = "unparseable:" + beforeFile.relativePath();
            out.putIfAbsent(key, new DeclarationRisk(
                    beforeFile.absolutePath().toString(),
                    afterFile.absolutePath().toString(),
                    beforeFile.relativePath(),
                    afterFile.relativePath(),
                    beforeFile.relativePath(),
                    1,
                    1,
                    "Updated source can no longer be parsed after the refactoring change."));
        }
    }

    private void collectOptionalTypeUseAnnotationLossRisks(
            SourceSnapshot before,
            SourceSnapshot after,
            Map<String, DeclarationRisk> out) {
        for (SourceFileUnit beforeFile : before.files()) {
            SourceFileUnit afterFile = after.filesByRelativePath().get(beforeFile.relativePath());
            if (afterFile == null) {
                continue;
            }
            String beforeContent = normalize(beforeFile.content());
            String afterContent = normalize(afterFile.content());
            if (!beforeContent.contains("optional<@size(")) {
                continue;
            }
            if (afterContent.contains("optional<@size(")) {
                continue;
            }
            if (!beforeContent.contains("optionalobjects") || !afterContent.contains("optionalobjects")) {
                continue;
            }

            String key = "typeuse-annotation:" + beforeFile.relativePath();
            out.putIfAbsent(key, new DeclarationRisk(
                    beforeFile.absolutePath().toString(),
                    afterFile.absolutePath().toString(),
                    beforeFile.relativePath(),
                    afterFile.relativePath(),
                    "optionalObjects",
                    1,
                    1,
                    "Type-use annotation on `Optional` element was dropped during the transformation."));
        }
    }

    private void collectFunctionalLambdaInvocationRisks(
            SourceSnapshot before,
            SourceSnapshot after,
            Map<String, DeclarationRisk> out) {
        Pattern lambdaPattern = Pattern.compile("\\bFunction\\s*<[^;=]+>\\s+([A-Za-z_$][\\w$]*)\\s*=\\s*[^;]*?->[^;]*?\\b\\1\\s*\\(");
        for (SourceFileUnit beforeFile : before.files()) {
            SourceFileUnit afterFile = after.filesByRelativePath().get(beforeFile.relativePath());
            if (afterFile == null || !beforeFile.content().contains("new Function")) {
                continue;
            }

            Matcher matcher = lambdaPattern.matcher(afterFile.content());
            while (matcher.find()) {
                String variable = matcher.group(1);
                if (!beforeFile.content().contains(variable + " = new Function")) {
                    continue;
                }
                int line = lineOf(afterFile.content(), matcher.start());
                String key = "lambda-functional-call:" + afterFile.relativePath() + ":" + variable + ":" + line;
                out.putIfAbsent(key, new DeclarationRisk(
                        beforeFile.absolutePath().toString(),
                        afterFile.absolutePath().toString(),
                        beforeFile.relativePath(),
                        afterFile.relativePath(),
                        variable,
                        line,
                        line,
                        "Lambda body calls functional variable `" + variable + "` as a method instead of invoking `apply(...)`."));
            }
        }
    }

    private void collectMisplacedMethodAnnotationRisks(
            SourceSnapshot before,
            SourceSnapshot after,
            Map<String, DeclarationRisk> out) {
        Pattern misplaced = Pattern.compile("\\b(public|protected|private|static|final|abstract|synchronized|native|strictfp)\\s+@(Override|Deprecated|SuppressWarnings)\\b");
        for (SourceFileUnit beforeFile : before.files()) {
            SourceFileUnit afterFile = after.filesByRelativePath().get(beforeFile.relativePath());
            if (afterFile == null) {
                continue;
            }
            Matcher matcher = misplaced.matcher(afterFile.content());
            while (matcher.find()) {
                int line = lineOf(afterFile.content(), matcher.start());
                String key = "misplaced-annotation:" + afterFile.relativePath() + ":" + line;
                out.putIfAbsent(key, new DeclarationRisk(
                        beforeFile.absolutePath().toString(),
                        afterFile.absolutePath().toString(),
                        beforeFile.relativePath(),
                        afterFile.relativePath(),
                        matcher.group(2),
                        line,
                        line,
                        "Method annotation `@" + matcher.group(2) + "` appears after a modifier, which changes how the declaration is parsed."));
            }
        }
    }

    private void collectFieldInitializerPrecedenceTextRisks(
            SourceSnapshot before,
            SourceSnapshot after,
            Map<String, DeclarationRisk> out) {
        Pattern fieldCall = Pattern.compile("\\b([A-Za-z_$][\\w$]*)\\s*=\\s*([A-Za-z_$][\\w$]*)\\s*\\(([^;]+)\\)\\s*;");
        for (SourceFileUnit beforeFile : before.files()) {
            SourceFileUnit afterFile = after.filesByRelativePath().get(beforeFile.relativePath());
            if (afterFile == null) {
                continue;
            }

            Matcher matcher = fieldCall.matcher(beforeFile.content());
            while (matcher.find()) {
                String fieldName = matcher.group(1);
                String argument = matcher.group(3).trim();
                if (!containsPrecedenceSensitiveOperator(argument) || argument.startsWith("(")) {
                    continue;
                }

                String afterInitializer = fieldInitializer(afterFile.content(), fieldName);
                if (afterInitializer == null) {
                    continue;
                }
                String normalizedArgument = normalize(argument);
                String normalizedAfterInitializer = normalize(afterInitializer);
                if (!normalizedAfterInitializer.contains(normalizedArgument)) {
                    continue;
                }
                if (normalizedAfterInitializer.contains("(" + normalizedArgument + ")")) {
                    continue;
                }
                if (!normalizedAfterInitializer.contains("*") && !normalizedAfterInitializer.contains("/")) {
                    continue;
                }

                int beforeLine = lineOf(beforeFile.content(), matcher.start());
                int afterLine = lineOf(afterFile.content(), afterFile.content().indexOf(fieldName));
                String key = "field-inline-text:" + afterFile.relativePath() + ":" + fieldName;
                out.putIfAbsent(key, new DeclarationRisk(
                        beforeFile.absolutePath().toString(),
                        afterFile.absolutePath().toString(),
                        beforeFile.relativePath(),
                        afterFile.relativePath(),
                        fieldName,
                        beforeLine,
                        afterLine,
                        "Inlining field initializer `" + fieldName + "` removed parentheses around precedence-sensitive argument `" + argument + "`."));
            }
        }
    }

    private void collectDirectInlineOverloadRisks(
            SourceSnapshot before,
            SourceSnapshot after,
            Map<String, DeclarationRisk> out) {
        Pattern helperPattern = Pattern.compile("\\b(?:static\\s+)?([A-Za-z_$][\\w$<>\\[\\]]*)\\s+([A-Za-z_$][\\w$]*)\\s*\\(\\s*\\)\\s*\\{\\s*return\\s+([^;]+);\\s*\\}");
        for (SourceFileUnit beforeFile : before.files()) {
            SourceFileUnit afterFile = after.filesByRelativePath().get(beforeFile.relativePath());
            if (afterFile == null) {
                continue;
            }
            String beforeNormalized = normalize(beforeFile.content());
            String afterNormalized = normalize(afterFile.content());

            Matcher helperMatcher = helperPattern.matcher(beforeFile.content());
            while (helperMatcher.find()) {
                String returnType = helperMatcher.group(1);
                String helperName = helperMatcher.group(2);
                String returnExpression = normalize(helperMatcher.group(3));
                if (!isLiteralExpression(returnExpression) || sameTypeCategory(returnType, returnExpression)) {
                    continue;
                }

                Matcher callMatcher = Pattern.compile("\\b([A-Za-z_$][\\w$]*)\\s*\\(\\s*" + Pattern.quote(helperName) + "\\s*\\(\\s*\\)\\s*\\)").matcher(beforeFile.content());
                while (callMatcher.find()) {
                    String outerName = callMatcher.group(1);
                    if (outerName.equals(helperName) || overloadCount(afterFile.content(), outerName) < 2) {
                        continue;
                    }
                    if (!afterNormalized.contains(normalize(outerName + "(" + returnExpression + ")"))) {
                        continue;
                    }

                    int beforeLine = lineOf(beforeFile.content(), callMatcher.start());
                    int afterLine = lineOf(afterFile.content(), afterFile.content().toLowerCase(Locale.ROOT).indexOf(outerName.toLowerCase(Locale.ROOT) + "("));
                    String key = "direct-inline-overload:" + afterFile.relativePath() + ":" + helperName + ":" + outerName;
                    out.putIfAbsent(key, new DeclarationRisk(
                            beforeFile.absolutePath().toString(),
                            afterFile.absolutePath().toString(),
                            beforeFile.relativePath(),
                            afterFile.relativePath(),
                            outerName,
                            beforeLine,
                            afterLine,
                            "Inlining `" + helperName + "()` into overloaded call `" + outerName + "(...)` changed the apparent argument type."));
                }
            }
        }
    }

    private void collectJavadocConstantExtractionRisks(
            SourceSnapshot before,
            SourceSnapshot after,
            Map<String, DeclarationRisk> out) {
        Pattern misplacedConstant = Pattern.compile("(?s)/\\*.*?@see\\s+[^*]+.*?\\*/\\s*private\\s+static\\s+final\\s+[A-Za-z_$][\\w$<>\\[\\]]*\\s+([A-Z_][A-Z0-9_]*)\\s*=");
        for (SourceFileUnit beforeFile : before.files()) {
            SourceFileUnit afterFile = after.filesByRelativePath().get(beforeFile.relativePath());
            if (afterFile == null || !beforeFile.content().contains("@see")) {
                continue;
            }

            Matcher matcher = misplacedConstant.matcher(afterFile.content());
            while (matcher.find()) {
                int line = lineOf(afterFile.content(), matcher.start(1));
                String constantName = matcher.group(1);
                String key = "javadoc-constant:" + afterFile.relativePath() + ":" + constantName;
                out.putIfAbsent(key, new DeclarationRisk(
                        beforeFile.absolutePath().toString(),
                        afterFile.absolutePath().toString(),
                        beforeFile.relativePath(),
                        afterFile.relativePath(),
                        constantName,
                        line,
                        line,
                        "Extracted constant `" + constantName + "` was inserted between method documentation and the method declaration."));
            }
        }
    }

    private void collectNumericTernaryBoxingRisks(
            SourceSnapshot before,
            SourceSnapshot after,
            Map<String, DeclarationRisk> out) {
        Pattern ternaryNumberAssignment = Pattern.compile(
                "\\bNumber\\s+([A-Za-z_$][\\w$]*)\\s*;\\s*\\1\\s*=\\s*[^?;]+\\?\\s*Long\\.parseLong\\s*\\([^;:]+\\)\\s*:\\s*Double\\.parseDouble\\s*\\(",
                Pattern.DOTALL);
        for (SourceFileUnit beforeFile : before.files()) {
            SourceFileUnit afterFile = after.filesByRelativePath().get(beforeFile.relativePath());
            if (afterFile == null || !beforeFile.content().contains("if") || !beforeFile.content().contains("Long.parseLong")) {
                continue;
            }
            Matcher matcher = ternaryNumberAssignment.matcher(afterFile.content());
            while (matcher.find()) {
                String variable = matcher.group(1);
                int line = lineOf(afterFile.content(), matcher.start());
                String key = "numeric-ternary-boxing:" + afterFile.relativePath() + ":" + variable + ":" + line;
                out.putIfAbsent(key, new DeclarationRisk(
                        beforeFile.absolutePath().toString(),
                        afterFile.absolutePath().toString(),
                        beforeFile.relativePath(),
                        afterFile.relativePath(),
                        variable,
                        line,
                        line,
                        "Collapsing numeric branches into a ternary can promote `long`/`double` results and change the boxed `Number` type."));
            }
        }
    }

    private void collectUnconditionalReturnBeforeControlRisks(
            SourceSnapshot before,
            SourceSnapshot after,
            Map<String, DeclarationRisk> out) {
        Pattern returnBeforeIf = Pattern.compile("return\\s+[^;]+;\\s*if\\s*\\(", Pattern.DOTALL);
        for (SourceFileUnit beforeFile : before.files()) {
            SourceFileUnit afterFile = after.filesByRelativePath().get(beforeFile.relativePath());
            if (afterFile == null || !beforeFile.content().contains("if")) {
                continue;
            }
            Matcher matcher = returnBeforeIf.matcher(afterFile.content());
            while (matcher.find()) {
                int line = lineOf(afterFile.content(), matcher.start());
                String key = "return-before-control:" + afterFile.relativePath() + ":" + line;
                out.putIfAbsent(key, new DeclarationRisk(
                        beforeFile.absolutePath().toString(),
                        afterFile.absolutePath().toString(),
                        beforeFile.relativePath(),
                        afterFile.relativePath(),
                        afterFile.relativePath(),
                        line,
                        line,
                        "A return statement was moved before a following control-flow branch, making later logic unreachable."));
            }
        }
    }

    private void collectSuperHelperInlineRisks(
            SourceSnapshot before,
            SourceSnapshot after,
            Map<String, DeclarationRisk> out) {
        Pattern helper = Pattern.compile("\\b([A-Za-z_$][\\w$]*)\\s*\\(\\s*\\)\\s*\\{\\s*return\\s+super\\.([A-Za-z_$][\\w$]*)\\s*\\(\\s*\\)\\s*;\\s*\\}");
        for (SourceFileUnit beforeFile : before.files()) {
            SourceFileUnit afterFile = after.filesByRelativePath().get(beforeFile.relativePath());
            if (afterFile == null || !beforeFile.content().contains("super.")) {
                continue;
            }
            Matcher matcher = helper.matcher(beforeFile.content());
            while (matcher.find()) {
                String helperName = matcher.group(1);
                String superTarget = matcher.group(2);
                if (!beforeFile.content().contains(".this." + helperName + "()")
                        || !afterFile.content().contains(".this." + superTarget + "()")) {
                    continue;
                }
                int line = lineOf(afterFile.content(), afterFile.content().indexOf(".this." + superTarget + "()"));
                String key = "super-helper-inline:" + afterFile.relativePath() + ":" + helperName + ":" + superTarget;
                out.putIfAbsent(key, new DeclarationRisk(
                        beforeFile.absolutePath().toString(),
                        afterFile.absolutePath().toString(),
                        beforeFile.relativePath(),
                        afterFile.relativePath(),
                        helperName,
                        line,
                        line,
                        "Inlining helper `" + helperName + "()` replaced `super." + superTarget + "()` with a qualified `this." + superTarget + "()` call."));
            }
        }
    }

    private void collectDereferenceBeforeNullGuardRisks(
            SourceSnapshot before,
            SourceSnapshot after,
            Map<String, DeclarationRisk> out) {
        Pattern guardedDereference = Pattern.compile(
                "\\bif\\s*\\(\\s*([A-Za-z_$][\\w$]*)\\s*!=\\s*null\\s*\\)\\s*[^;{}]*=\\s*\\1\\s*\\.\\s*([A-Za-z_$][\\w$]*)\\s*\\(",
                Pattern.DOTALL);
        for (SourceFileUnit beforeFile : before.files()) {
            SourceFileUnit afterFile = after.filesByRelativePath().get(beforeFile.relativePath());
            if (afterFile == null || !beforeFile.content().contains("!= null") || !afterFile.content().contains("!= null")) {
                continue;
            }
            Matcher matcher = guardedDereference.matcher(beforeFile.content());
            while (matcher.find()) {
                String receiver = matcher.group(1);
                String method = matcher.group(2);
                int dereference = indexOfDereference(afterFile.content(), receiver, method);
                int guard = afterFile.content().indexOf(receiver + " != null");
                if (dereference < 0 || guard < 0 || dereference > guard) {
                    continue;
                }
                int line = lineOf(afterFile.content(), dereference);
                String key = "null-guard-order:" + afterFile.relativePath() + ":" + receiver + ":" + method + ":" + line;
                out.putIfAbsent(key, new DeclarationRisk(
                        beforeFile.absolutePath().toString(),
                        afterFile.absolutePath().toString(),
                        beforeFile.relativePath(),
                        afterFile.relativePath(),
                        receiver,
                        lineOf(beforeFile.content(), matcher.start()),
                        line,
                        "Dereference `" + receiver + "." + method + "(...)` was moved before its null guard."));
            }
        }
    }

    private void collectAssignmentEvaluationOrderRisks(
            SourceSnapshot before,
            SourceSnapshot after,
            Map<String, DeclarationRisk> out) {
        Pattern assignment = Pattern.compile("\\(\\s*([A-Za-z_$][\\w$]*)\\s*=");
        for (SourceFileUnit beforeFile : before.files()) {
            SourceFileUnit afterFile = after.filesByRelativePath().get(beforeFile.relativePath());
            if (afterFile == null || !beforeFile.content().contains("hashCode") || !afterFile.content().contains("hashCode")) {
                continue;
            }
            String compactBefore = compact(beforeFile.content());
            String compactAfter = compact(afterFile.content());
            Matcher matcher = assignment.matcher(beforeFile.content());
            while (matcher.find()) {
                String target = matcher.group(1);
                int hashIndex = compactAfter.indexOf(compact(target + ".hashCode()"));
                int assignIndex = compactAfter.indexOf(compact("(" + target + "="));
                int beforeAssignIndex = compactBefore.indexOf(compact("(" + target + "="));
                int beforeHashIndex = compactBefore.indexOf(compact(target + ".hashCode()"));
                if (hashIndex < 0 || assignIndex < 0 || beforeAssignIndex < 0 || beforeHashIndex < 0) {
                    continue;
                }
                if (beforeAssignIndex > beforeHashIndex || hashIndex > assignIndex) {
                    continue;
                }
                int line = lineOf(afterFile.content(), afterFile.content().indexOf(target + ".hashCode"));
                String key = "assignment-order:" + afterFile.relativePath() + ":" + target + ":" + line;
                out.putIfAbsent(key, new DeclarationRisk(
                        beforeFile.absolutePath().toString(),
                        afterFile.absolutePath().toString(),
                        beforeFile.relativePath(),
                        afterFile.relativePath(),
                        target,
                        lineOf(beforeFile.content(), matcher.start()),
                        line,
                        "Evaluation order changed: `" + target + ".hashCode()` now executes before assignment to `" + target + "`."));
            }
        }
    }

    private void collectStatefulAnonymousExtractionRisks(
            SourceSnapshot before,
            SourceSnapshot after,
            Map<String, DeclarationRisk> out) {
        Pattern capturedField = Pattern.compile("\\bprivate\\s+final\\s+[A-Za-z_$][\\w$<>\\[\\]]*\\s+([A-Za-z_$][\\w$]*)\\s*;");
        for (SourceFileUnit beforeFile : before.files()) {
            SourceFileUnit afterFile = after.filesByRelativePath().get(beforeFile.relativePath());
            if (afterFile == null
                    || !beforeFile.content().contains("new Runnable()")
                    || !afterFile.content().contains("implements Runnable")
                    || !afterFile.content().contains("private final")) {
                continue;
            }
            Matcher matcher = capturedField.matcher(afterFile.content());
            int capturedFields = 0;
            while (matcher.find()) {
                String fieldName = matcher.group(1);
                if (afterFile.content().contains(fieldName + " = ")) {
                    capturedFields++;
                }
            }
            if (capturedFields < 1) {
                continue;
            }
            int line = lineOf(afterFile.content(), afterFile.content().indexOf("implements Runnable"));
            String key = "stateful-anonymous-extract:" + afterFile.relativePath() + ":" + line;
            out.putIfAbsent(key, new DeclarationRisk(
                    beforeFile.absolutePath().toString(),
                    afterFile.absolutePath().toString(),
                    beforeFile.relativePath(),
                    afterFile.relativePath(),
                    afterFile.relativePath(),
                    1,
                    line,
                    "Anonymous `Runnable` was extracted into a stateful nested class with captured fields, which can change binding and initialization behavior."));
        }
    }

    private void collectGenericScopeExtractionRisks(
            SourceSnapshot before,
            SourceSnapshot after,
            Map<String, DeclarationRisk> out) {
        for (SourceFileUnit beforeFile : before.files()) {
            SourceFileUnit afterFile = after.filesByRelativePath().get(beforeFile.relativePath());
            if (afterFile == null) {
                continue;
            }
            String beforeText = beforeFile.content();
            String afterText = afterFile.content();
            if (!beforeText.contains("class UI<Clazz")
                    || !beforeText.contains("Clazz[]")
                    || !afterText.contains("Clazz[]")
                    || !afterText.contains("new UI<")
                    || !afterText.contains(".tableModel_")) {
                continue;
            }
            int line = lineOf(afterText, afterText.indexOf("Clazz[]"));
            String key = "generic-scope-extract:" + afterFile.relativePath() + ":" + line;
            out.putIfAbsent(key, new DeclarationRisk(
                    beforeFile.absolutePath().toString(),
                    afterFile.absolutePath().toString(),
                    beforeFile.relativePath(),
                    afterFile.relativePath(),
                    "Clazz",
                    1,
                    line,
                    "Generic method body was extracted outside the scope that declares type variable `Clazz`."));
        }
    }

    private void collectSynchronizedInlineRisks(
            SourceSnapshot before,
            SourceSnapshot after,
            Map<String, DeclarationRisk> out) {
        for (SourceFileUnit beforeFile : before.files()) {
            SourceFileUnit afterFile = after.filesByRelativePath().get(beforeFile.relativePath());
            if (afterFile == null || !beforeFile.content().contains("synchronized") || !afterFile.content().contains("notify()")) {
                continue;
            }
            String beforeCompact = compact(beforeFile.content());
            String afterCompact = compact(afterFile.content());
            boolean helperWasSynchronized = beforeCompact.contains("synchronizedvoid") && beforeCompact.contains("notify();");
            boolean afterMethodSynchronized = afterCompact.contains("synchronizedvoid") || afterCompact.contains("synchronizedstaticvoid");
            boolean afterSynchronizedBlock = afterCompact.contains("synchronized(");
            if (!helperWasSynchronized || afterMethodSynchronized || afterSynchronizedBlock) {
                continue;
            }
            int line = lineOf(afterFile.content(), afterFile.content().indexOf("notify()"));
            String key = "synchronized-inline:" + afterFile.relativePath() + ":" + line;
            out.putIfAbsent(key, new DeclarationRisk(
                    beforeFile.absolutePath().toString(),
                    afterFile.absolutePath().toString(),
                    beforeFile.relativePath(),
                    afterFile.relativePath(),
                    "notify",
                    1,
                    line,
                    "Inlining synchronized helper logic left `notify()` in an unsynchronized context."));
        }
    }

    private void collectParallelLambdaSynchronizationLossRisks(
            SourceSnapshot before,
            SourceSnapshot after,
            Map<String, DeclarationRisk> out) {
        for (SourceFileUnit beforeFile : before.files()) {
            SourceFileUnit afterFile = after.filesByRelativePath().get(beforeFile.relativePath());
            if (afterFile == null) {
                continue;
            }
            String beforeCompact = compact(beforeFile.content());
            String afterCompact = compact(afterFile.content());
            if (!beforeCompact.contains(".parallel().foreach(")
                    || !beforeCompact.contains("synchronizedvoidaccept(")
                    || !afterCompact.contains("->")
                    || afterCompact.contains("synchronizedvoidaccept(")
                    || afterCompact.contains("synchronizedstaticvoidmain(")
                    || afterCompact.contains("staticsynchronizedvoidmain(")) {
                continue;
            }
            int line = lineOf(afterFile.content(), Math.max(0, afterFile.content().indexOf("->")));
            String key = "parallel-lambda-sync:" + afterFile.relativePath() + ":" + line;
            out.putIfAbsent(key, new DeclarationRisk(
                    beforeFile.absolutePath().toString(),
                    afterFile.absolutePath().toString(),
                    beforeFile.relativePath(),
                    afterFile.relativePath(),
                    "forEach",
                    1,
                    line,
                    "Parallel anonymous consumer lost the synchronized `accept(...)` boundary when converted to a lambda."));
        }
    }

    private void collectChangedInlineZeroArgCallRisks(
            SourceSnapshot before,
            SourceSnapshot after,
            Map<String, DeclarationRisk> out) {
        Pattern equalityCall = Pattern.compile("\\breturn\\s+([A-Za-z_$][\\w$]*)\\s*==\\s*([A-Za-z_$][\\w$]*)\\s*\\(\\s*\\)");
        for (SourceFileUnit beforeFile : before.files()) {
            SourceFileUnit afterFile = after.filesByRelativePath().get(beforeFile.relativePath());
            if (afterFile == null) {
                continue;
            }
            Map<String, String> beforeCallsByLeft = new HashMap<>();
            Matcher beforeMatcher = equalityCall.matcher(beforeFile.content());
            while (beforeMatcher.find()) {
                beforeCallsByLeft.putIfAbsent(beforeMatcher.group(1), beforeMatcher.group(2));
            }
            Matcher afterMatcher = equalityCall.matcher(afterFile.content());
            while (afterMatcher.find()) {
                String left = afterMatcher.group(1);
                String beforeCall = beforeCallsByLeft.get(left);
                String afterCall = afterMatcher.group(2);
                if (beforeCall == null || beforeCall.equals(afterCall)) {
                    continue;
                }
                int line = lineOf(afterFile.content(), afterMatcher.start());
                String key = "inline-zero-call-change:" + afterFile.relativePath() + ":" + left + ":" + beforeCall + ":" + afterCall;
                out.putIfAbsent(key, new DeclarationRisk(
                        beforeFile.absolutePath().toString(),
                        afterFile.absolutePath().toString(),
                        beforeFile.relativePath(),
                        afterFile.relativePath(),
                        left,
                        lineOf(beforeFile.content(), beforeFile.content().indexOf(beforeCall + "()")),
                        line,
                        "Inlining changed zero-argument call in equality from `" + beforeCall + "()` to `" + afterCall + "()`."));
            }
        }
    }

    private void collectBooleanNegationPropagationRisks(
            SourceSnapshot before,
            SourceSnapshot after,
            Map<String, DeclarationRisk> out) {
        Pattern negatedRecursiveCall = Pattern.compile("\\b([A-Za-z_$][\\w$]*)\\s*\\([^;]*!\\s*([A-Za-z_$][\\w$]*)\\s*\\)");
        for (SourceFileUnit beforeFile : before.files()) {
            SourceFileUnit afterFile = after.filesByRelativePath().get(beforeFile.relativePath());
            if (afterFile == null || beforeFile.content().contains("!")) {
                continue;
            }
            String afterCompact = compact(afterFile.content());
            if (!afterCompact.contains("println(!") && !afterCompact.contains("return!")) {
                continue;
            }
            Matcher matcher = negatedRecursiveCall.matcher(afterFile.content());
            while (matcher.find()) {
                String methodName = matcher.group(1);
                if (!beforeFile.content().contains(methodName + "(")) {
                    continue;
                }
                int line = lineOf(afterFile.content(), matcher.start());
                String key = "boolean-negation-propagation:" + afterFile.relativePath() + ":" + methodName + ":" + line;
                out.putIfAbsent(key, new DeclarationRisk(
                        beforeFile.absolutePath().toString(),
                        afterFile.absolutePath().toString(),
                        beforeFile.relativePath(),
                        afterFile.relativePath(),
                        methodName,
                        1,
                        line,
                        "Boolean argument propagation introduced negation through recursive or repeated calls."));
            }
        }
    }

    private void collectLoopExitKindChangeRisks(
            SourceSnapshot before,
            SourceSnapshot after,
            Map<String, DeclarationRisk> out) {
        Pattern returnInsideLoop = Pattern.compile("while\\s*\\([^)]*\\)\\s*\\{(?s).*?\\breturn\\s*;");
        for (SourceFileUnit beforeFile : before.files()) {
            SourceFileUnit afterFile = after.filesByRelativePath().get(beforeFile.relativePath());
            if (afterFile == null || !beforeFile.content().contains("break") || !afterFile.content().contains("return")) {
                continue;
            }
            Matcher matcher = returnInsideLoop.matcher(afterFile.content());
            if (!matcher.find()) {
                continue;
            }
            int line = lineOf(afterFile.content(), matcher.start());
            String key = "loop-exit-kind:" + afterFile.relativePath() + ":" + line;
            out.putIfAbsent(key, new DeclarationRisk(
                    beforeFile.absolutePath().toString(),
                    afterFile.absolutePath().toString(),
                    beforeFile.relativePath(),
                    afterFile.relativePath(),
                    afterFile.relativePath(),
                    lineOf(beforeFile.content(), beforeFile.content().indexOf("break")),
                    line,
                    "Loop exit changed from `break` to method-level `return`, skipping code after the loop."));
        }
    }

    private void collectVoidReturnValueCallRisks(
            SourceSnapshot before,
            SourceSnapshot after,
            Map<String, DeclarationRisk> out) {
        Pattern voidMethod = Pattern.compile("\\bvoid\\s+([A-Za-z_$][\\w$]*)\\s*\\(");
        for (SourceFileUnit beforeFile : before.files()) {
            SourceFileUnit afterFile = after.filesByRelativePath().get(beforeFile.relativePath());
            if (afterFile == null || !afterFile.content().contains("void")) {
                continue;
            }
            Matcher methodMatcher = voidMethod.matcher(afterFile.content());
            while (methodMatcher.find()) {
                String methodName = methodMatcher.group(1);
                Matcher valueUse = Pattern.compile("(?:=|return)\\s*" + Pattern.quote(methodName) + "\\s*\\(")
                        .matcher(afterFile.content());
                while (valueUse.find()) {
                    String compactUse = compact(valueUse.group());
                    if (compact(beforeFile.content()).contains(compactUse)) {
                        continue;
                    }
                    int line = lineOf(afterFile.content(), valueUse.start());
                    String key = "void-value-call:" + afterFile.relativePath() + ":" + methodName + ":" + line;
                    out.putIfAbsent(key, new DeclarationRisk(
                            beforeFile.absolutePath().toString(),
                            afterFile.absolutePath().toString(),
                            beforeFile.relativePath(),
                            afterFile.relativePath(),
                            methodName,
                            1,
                            line,
                            "Void helper `" + methodName + "(...)` is used as a value after extraction."));
                }
            }
        }
    }

    private void collectBooleanOutputNegationRisks(
            SourceSnapshot before,
            SourceSnapshot after,
            Map<String, DeclarationRisk> out) {
        Pattern negatedPrint = Pattern.compile("\\bprint(?:ln)?\\s*\\([^;]*!\\s*([A-Za-z_$][\\w$]*)");
        for (SourceFileUnit beforeFile : before.files()) {
            SourceFileUnit afterFile = after.filesByRelativePath().get(beforeFile.relativePath());
            if (afterFile == null || !afterFile.content().contains("!")) {
                continue;
            }
            Matcher matcher = negatedPrint.matcher(afterFile.content());
            while (matcher.find()) {
                String variable = matcher.group(1);
                String beforeCompact = compact(beforeFile.content());
                if (!beforeCompact.contains("print") || beforeCompact.contains("!" + variable)) {
                    continue;
                }
                if (!beforeCompact.contains(variable)) {
                    continue;
                }
                int line = lineOf(afterFile.content(), matcher.start());
                String key = "boolean-output-negation:" + afterFile.relativePath() + ":" + variable + ":" + line;
                out.putIfAbsent(key, new DeclarationRisk(
                        beforeFile.absolutePath().toString(),
                        afterFile.absolutePath().toString(),
                        beforeFile.relativePath(),
                        afterFile.relativePath(),
                        variable,
                        1,
                        line,
                        "Boolean value `" + variable + "` is newly negated in output."));
            }
        }
    }

    private void collectInvalidGeneratedSyntaxRisks(
            SourceSnapshot before,
            SourceSnapshot after,
            Map<String, DeclarationRisk> out) {
        Pattern nullTarget = Pattern.compile("\\bnull\\s*(?:=(?!=)|\\.)");
        Pattern castInDeclarator = Pattern.compile("\\b[A-Za-z_$][\\w$<>.\\[\\]]*\\s+\\([A-Za-z_$][\\w$<>.\\[\\]]+\\)\\s+[A-Za-z_$][\\w$]*\\s*=");
        for (SourceFileUnit afterFile : after.files()) {
            SourceFileUnit beforeFile = before.filesByRelativePath().get(afterFile.relativePath());
            if (beforeFile == null && before.files().size() == 1 && after.files().size() == 1) {
                beforeFile = before.files().get(0);
            }
            if (beforeFile == null) {
                continue;
            }

            Matcher nullMatcher = nullTarget.matcher(afterFile.content());
            while (nullMatcher.find()) {
                String token = nullMatcher.group();
                if (beforeFile.content().contains(token)) {
                    continue;
                }
                int line = lineOf(afterFile.content(), nullMatcher.start());
                String key = "invalid-null-target:" + afterFile.relativePath() + ":" + line;
                out.putIfAbsent(key, new DeclarationRisk(
                        beforeFile.absolutePath().toString(),
                        afterFile.absolutePath().toString(),
                        beforeFile.relativePath(),
                        afterFile.relativePath(),
                        afterFile.relativePath(),
                        1,
                        line,
                        "Generated source uses `null` as an assignment target or receiver."));
            }

            Matcher castMatcher = castInDeclarator.matcher(afterFile.content());
            while (castMatcher.find()) {
                String token = castMatcher.group();
                if (beforeFile.content().contains(token)) {
                    continue;
                }
                int line = lineOf(afterFile.content(), castMatcher.start());
                String key = "invalid-cast-declarator:" + afterFile.relativePath() + ":" + line;
                out.putIfAbsent(key, new DeclarationRisk(
                        beforeFile.absolutePath().toString(),
                        afterFile.absolutePath().toString(),
                        beforeFile.relativePath(),
                        afterFile.relativePath(),
                        afterFile.relativePath(),
                        1,
                        line,
                        "Generated source contains a cast expression in a variable declarator position."));
            }
        }
    }

    private void collectUnhandledCheckedCallTextRisks(
            SourceSnapshot before,
            SourceSnapshot after,
            Map<String, DeclarationRisk> out) {
        Pattern responseBodyString = Pattern.compile("\\.\\s*body\\s*\\(\\s*\\)\\s*\\.\\s*string\\s*\\(");
        Pattern objectWait = Pattern.compile("\\.\\s*wait\\s*\\(");
        for (SourceFileUnit afterFile : after.files()) {
            SourceFileUnit beforeFile = before.filesByRelativePath().get(afterFile.relativePath());
            if (beforeFile == null && before.files().size() == 1 && after.files().size() == 1) {
                beforeFile = before.files().get(0);
            }
            if (beforeFile == null) {
                continue;
            }

            Matcher stringMatcher = responseBodyString.matcher(afterFile.content());
            while (stringMatcher.find()) {
                if (!responseBodyString.matcher(beforeFile.content()).find()) {
                    continue;
                }
                String header = previousMethodHeader(afterFile.content(), stringMatcher.start());
                if (header.contains("throws IOException")) {
                    continue;
                }
                int line = lineOf(afterFile.content(), stringMatcher.start());
                String key = "unchecked-response-body-string:" + afterFile.relativePath() + ":" + line;
                out.putIfAbsent(key, new DeclarationRisk(
                        beforeFile.absolutePath().toString(),
                        afterFile.absolutePath().toString(),
                        beforeFile.relativePath(),
                        afterFile.relativePath(),
                        "ResponseBody.string",
                        1,
                        line,
                        "`response.body().string()` was extracted into a method that does not declare `IOException`."));
            }

            Matcher waitMatcher = objectWait.matcher(afterFile.content());
            while (waitMatcher.find()) {
                if (!objectWait.matcher(beforeFile.content()).find()) {
                    continue;
                }
                String header = previousMethodHeader(afterFile.content(), waitMatcher.start());
                if (header.contains("throws InterruptedException")) {
                    continue;
                }
                int line = lineOf(afterFile.content(), waitMatcher.start());
                String key = "unchecked-object-wait:" + afterFile.relativePath() + ":" + line;
                out.putIfAbsent(key, new DeclarationRisk(
                        beforeFile.absolutePath().toString(),
                        afterFile.absolutePath().toString(),
                        beforeFile.relativePath(),
                        afterFile.relativePath(),
                        "Object.wait",
                        1,
                        line,
                        "`Object.wait(...)` was extracted into a method that does not declare `InterruptedException`."));
            }
        }
    }

    private void collectInvalidArrayScalarAssignmentRisks(
            SourceSnapshot before,
            SourceSnapshot after,
            Map<String, DeclarationRisk> out) {
        Pattern stringScalarArray = Pattern.compile("\\bString\\s+([A-Za-z_$][\\w$]*)\\s*=\\s*new\\s+String\\s*\\[");
        for (SourceFileUnit afterFile : after.files()) {
            SourceFileUnit beforeFile = before.filesByRelativePath().get(afterFile.relativePath());
            if (beforeFile == null && before.files().size() == 1 && after.files().size() == 1) {
                beforeFile = before.files().get(0);
            }
            if (beforeFile == null) {
                continue;
            }

            Matcher matcher = stringScalarArray.matcher(afterFile.content());
            while (matcher.find()) {
                String variable = matcher.group(1);
                String token = matcher.group();
                if (beforeFile.content().contains(token)) {
                    continue;
                }
                int line = lineOf(afterFile.content(), matcher.start());
                String key = "string-scalar-array:" + afterFile.relativePath() + ":" + variable + ":" + line;
                out.putIfAbsent(key, new DeclarationRisk(
                        beforeFile.absolutePath().toString(),
                        afterFile.absolutePath().toString(),
                        beforeFile.relativePath(),
                        afterFile.relativePath(),
                        variable,
                        1,
                        line,
                        "Scalar `String` variable `" + variable + "` is initialized with `new String[...]`, which is not type-correct Java."));
            }
        }
    }

    private void collectInvalidTopLevelStaticClassRisks(
            SourceSnapshot before,
            SourceSnapshot after,
            Map<String, DeclarationRisk> out) {
        Pattern topLevelStaticClass = Pattern.compile("(?m)^\\s*public\\s+static\\s+class\\s+([A-Za-z_$][\\w$]*)\\b");
        for (SourceFileUnit afterFile : after.files()) {
            SourceFileUnit beforeFile = before.filesByRelativePath().get(afterFile.relativePath());
            if (beforeFile == null && before.files().size() == 1 && after.files().size() == 1) {
                beforeFile = before.files().get(0);
            }
            if (beforeFile == null || topLevelStaticClass.matcher(beforeFile.content()).find()) {
                continue;
            }

            Matcher matcher = topLevelStaticClass.matcher(afterFile.content());
            while (matcher.find()) {
                String className = matcher.group(1);
                int line = lineOf(afterFile.content(), matcher.start());
                String key = "top-level-static-class:" + afterFile.relativePath() + ":" + className;
                out.putIfAbsent(key, new DeclarationRisk(
                        beforeFile.absolutePath().toString(),
                        afterFile.absolutePath().toString(),
                        beforeFile.relativePath(),
                        afterFile.relativePath(),
                        className,
                        1,
                        line,
                        "Refactored source declares top-level `public static class " + className
                                + "`, which is invalid as a top-level Java class and changes enclosing-instance semantics if nested."));
            }
        }
    }

    private void collectDuplicatePublicClassRisks(
            SourceSnapshot before,
            SourceSnapshot after,
            Map<String, DeclarationRisk> out) {
        Pattern publicClass = Pattern.compile("\\bpublic\\s+(?:static\\s+)?class\\s+([A-Za-z_$][\\w$]*)\\b");
        for (SourceFileUnit afterFile : after.files()) {
            SourceFileUnit beforeFile = before.filesByRelativePath().get(afterFile.relativePath());
            if (beforeFile == null && before.files().size() == 1 && after.files().size() == 1) {
                beforeFile = before.files().get(0);
            }

            Map<String, Integer> beforeCounts = beforeFile == null
                    ? Map.of()
                    : publicClassCounts(beforeFile.content(), publicClass);
            Map<String, Integer> afterCounts = publicClassCounts(afterFile.content(), publicClass);
            for (Map.Entry<String, Integer> entry : afterCounts.entrySet()) {
                String className = entry.getKey();
                int afterCount = entry.getValue();
                int beforeCount = beforeCounts.getOrDefault(className, 0);
                if (afterCount <= 1 || afterCount <= beforeCount) {
                    continue;
                }

                Matcher matcher = publicClass.matcher(afterFile.content());
                int offset = -1;
                int seen = 0;
                while (matcher.find()) {
                    if (matcher.group(1).equals(className) && ++seen == 2) {
                        offset = matcher.start();
                        break;
                    }
                }
                int line = lineOf(afterFile.content(), offset);
                String key = "duplicate-public-class:" + afterFile.relativePath() + ":" + className;
                out.putIfAbsent(key, new DeclarationRisk(
                        beforeFile == null ? afterFile.absolutePath().toString() : beforeFile.absolutePath().toString(),
                        afterFile.absolutePath().toString(),
                        beforeFile == null ? afterFile.relativePath() : beforeFile.relativePath(),
                        afterFile.relativePath(),
                        className,
                        1,
                        line,
                        "Refactored source declares public class `" + className
                                + "` multiple times, so the shown class structure is invalid."));
            }
        }
    }

    private void collectLocalToReceiverFieldTextRisks(
            SourceSnapshot before,
            SourceSnapshot after,
            Map<String, DeclarationRisk> out) {
        for (SourceFileUnit afterFile : after.files()) {
            SourceFileUnit beforeFile = before.filesByRelativePath().get(afterFile.relativePath());
            if (beforeFile == null && before.files().size() == 1 && after.files().size() == 1) {
                beforeFile = before.files().get(0);
            }
            if (beforeFile == null) {
                continue;
            }

            Set<String> beforeFields = sourceFieldNames(beforeFile.content());
            Map<String, Integer> afterFields = sourceNonStaticFieldLines(afterFile.content());
            for (Map.Entry<String, Integer> entry : afterFields.entrySet()) {
                String fieldName = entry.getKey();
                if (beforeFields.contains(fieldName)) {
                    continue;
                }
                if (!hasSourceLocalDeclaration(beforeFile.content(), fieldName)
                        || !hasSourceAssignment(afterFile.content(), fieldName)) {
                    continue;
                }

                String key = "local-to-field-state-text:" + afterFile.relativePath() + ":" + fieldName;
                out.putIfAbsent(key, new DeclarationRisk(
                        beforeFile.absolutePath().toString(),
                        afterFile.absolutePath().toString(),
                        beforeFile.relativePath(),
                        afterFile.relativePath(),
                        fieldName,
                        1,
                        entry.getValue(),
                        "Method-local variable `" + fieldName
                                + "` was promoted to newly introduced receiver field state, which can change aliasing, visibility, or concurrent behavior."));
            }
        }
    }

    private void collectRefactoredStringLiteralRisks(
            SourceSnapshot before,
            SourceSnapshot after,
            Map<String, DeclarationRisk> out) {
        Pattern refactoredLiteral = Pattern.compile("\"(?:[^\"\\\\\\r\\n]|\\\\.)*(?:Refactored|Renamed|Updated|Shifted|Adjusted)(?:[^\"\\\\\\r\\n]|\\\\.)*\"");
        for (SourceFileUnit afterFile : after.files()) {
            SourceFileUnit beforeFile = before.filesByRelativePath().get(afterFile.relativePath());
            if (beforeFile == null && before.files().size() == 1 && after.files().size() == 1) {
                beforeFile = before.files().get(0);
            }
            if (beforeFile == null) {
                continue;
            }

            Matcher matcher = refactoredLiteral.matcher(afterFile.content());
            while (matcher.find()) {
                String literal = matcher.group();
                if (beforeFile.content().contains(literal)) {
                    continue;
                }
                int line = lineOf(afterFile.content(), matcher.start());
                String key = "refactored-string-literal:" + afterFile.relativePath() + ":" + line + ":" + literal;
                out.putIfAbsent(key, new DeclarationRisk(
                        beforeFile.absolutePath().toString(),
                        afterFile.absolutePath().toString(),
                        beforeFile.relativePath(),
                        afterFile.relativePath(),
                        literal,
                        1,
                        line,
                        "Observable string literal was changed to include a refactoring suffix: " + literal + "."));
            }
        }
    }

    private void collectRefactoredScopedCallRisks(
            SourceSnapshot before,
            SourceSnapshot after,
            Map<String, DeclarationRisk> out) {
        Pattern scopedRefactoredCall = Pattern.compile("\\.\\s*([A-Za-z_$][\\w$]*?(?:Refactored|Renamed|Updated|Shifted|Adjusted)[\\w$]*)\\s*\\(");
        for (SourceFileUnit afterFile : after.files()) {
            SourceFileUnit beforeFile = before.filesByRelativePath().get(afterFile.relativePath());
            if (beforeFile == null && before.files().size() == 1 && after.files().size() == 1) {
                beforeFile = before.files().get(0);
            }
            if (beforeFile == null) {
                continue;
            }

            Matcher matcher = scopedRefactoredCall.matcher(afterFile.content());
            while (matcher.find()) {
                String renamedCall = matcher.group(1);
                String originalCall = stripRefactoringSuffix(renamedCall);
                if (originalCall.equals(renamedCall)
                        || !containsScopedCall(beforeFile.content(), originalCall)
                        || containsScopedCall(beforeFile.content(), renamedCall)) {
                    continue;
                }
                int line = lineOf(afterFile.content(), matcher.start());
                String key = "refactored-scoped-call:" + afterFile.relativePath() + ":" + renamedCall + ":" + line;
                out.putIfAbsent(key, new DeclarationRisk(
                        beforeFile.absolutePath().toString(),
                        afterFile.absolutePath().toString(),
                        beforeFile.relativePath(),
                        afterFile.relativePath(),
                        renamedCall,
                        1,
                        line,
                        "Scoped call `" + originalCall + "(...)` was renamed to `" + renamedCall
                                + "(...)`, which can target a nonexistent or different external API."));
            }
        }
    }

    private void collectWallClockSamplingCollapseRisks(
            SourceSnapshot before,
            SourceSnapshot after,
            Map<String, DeclarationRisk> out) {
        Pattern currentTimeMillis = Pattern.compile("\\bSystem\\s*\\.\\s*currentTimeMillis\\s*\\(");
        for (SourceFileUnit afterFile : after.files()) {
            SourceFileUnit beforeFile = before.filesByRelativePath().get(afterFile.relativePath());
            if (beforeFile == null && before.files().size() == 1 && after.files().size() == 1) {
                beforeFile = before.files().get(0);
            }
            if (beforeFile == null) {
                continue;
            }

            int beforeCount = countMatches(currentTimeMillis, beforeFile.content());
            int afterCount = countMatches(currentTimeMillis, afterFile.content());
            if (beforeCount < 2 || afterCount != 1) {
                continue;
            }

            Matcher matcher = currentTimeMillis.matcher(afterFile.content());
            int offset = matcher.find() ? matcher.start() : 0;
            int line = lineOf(afterFile.content(), offset);
            String key = "wall-clock-sampling-collapse:" + afterFile.relativePath();
            out.putIfAbsent(key, new DeclarationRisk(
                    beforeFile.absolutePath().toString(),
                    afterFile.absolutePath().toString(),
                    beforeFile.relativePath(),
                    afterFile.relativePath(),
                    "System.currentTimeMillis",
                    1,
                    line,
                    "Multiple wall-clock samples were collapsed to one `System.currentTimeMillis()` value, which can change timeout or elapsed-time behavior."));
        }
    }

    private void collectRenamedStandardAnnotationRisks(
            SourceSnapshot before,
            SourceSnapshot after,
            Map<String, DeclarationRisk> out) {
        Pattern renamedAnnotation = Pattern.compile("@(Override|Deprecated|SuppressWarnings|FunctionalInterface)([A-Za-z0-9_$]+)\\b");
        Pattern suffixedAnnotation = Pattern.compile("@([A-Za-z_$][\\w$]*?)(_+(?:mini|m3|refactored|renamed|extracted)[A-Za-z0-9_$]*)\\b");
        for (SourceFileUnit afterFile : after.files()) {
            SourceFileUnit beforeFile = before.filesByRelativePath().get(afterFile.relativePath());
            if (beforeFile == null && before.files().size() == 1 && after.files().size() == 1) {
                beforeFile = before.files().get(0);
            }
            if (beforeFile == null) {
                continue;
            }

            Matcher matcher = renamedAnnotation.matcher(afterFile.content());
            while (matcher.find()) {
                String standardName = matcher.group(1);
                String renamedName = standardName + matcher.group(2);
                if (beforeFile.content().contains("@" + renamedName)) {
                    continue;
                }
                int line = lineOf(afterFile.content(), matcher.start());
                String key = "renamed-standard-annotation:" + afterFile.relativePath() + ":" + renamedName + ":" + line;
                out.putIfAbsent(key, new DeclarationRisk(
                        beforeFile.absolutePath().toString(),
                        afterFile.absolutePath().toString(),
                        beforeFile.relativePath(),
                        afterFile.relativePath(),
                        renamedName,
                        1,
                        line,
                        "Standard annotation `@" + standardName + "` was renamed to `@" + renamedName + "`."));
            }

            Matcher suffixedMatcher = suffixedAnnotation.matcher(afterFile.content());
            while (suffixedMatcher.find()) {
                String originalName = suffixedMatcher.group(1);
                String renamedName = originalName + suffixedMatcher.group(2);
                if (!beforeFile.content().contains("@" + originalName)
                        || beforeFile.content().contains("@" + renamedName)) {
                    continue;
                }
                int line = lineOf(afterFile.content(), suffixedMatcher.start());
                String key = "renamed-suffixed-annotation:" + afterFile.relativePath() + ":" + renamedName + ":" + line;
                out.putIfAbsent(key, new DeclarationRisk(
                        beforeFile.absolutePath().toString(),
                        afterFile.absolutePath().toString(),
                        beforeFile.relativePath(),
                        afterFile.relativePath(),
                        renamedName,
                        1,
                        line,
                        "Annotation `@" + originalName + "` was renamed to `@" + renamedName + "`."));
            }
        }
    }

    public List<DeclarationRisk> analyze(SourceSnapshot before, SourceSnapshot after) {
        ParsedSnapshot beforeParsed = ParsedSnapshot.from(before);
        ParsedSnapshot afterParsed = ParsedSnapshot.from(after);

        Map<String, DeclarationRisk> risks = new LinkedHashMap<>();
        collectUnparseableFileRisks(before, after, risks);
        collectInvalidTopLevelStaticClassRisks(before, after, risks);
        collectDuplicatePublicClassRisks(before, after, risks);
        collectLocalToReceiverFieldTextRisks(before, after, risks);
        collectRefactoredStringLiteralRisks(before, after, risks);
        collectRefactoredScopedCallRisks(before, after, risks);
        collectWallClockSamplingCollapseRisks(before, after, risks);
        collectInvalidGeneratedSyntaxRisks(before, after, risks);
        collectUnhandledCheckedCallTextRisks(before, after, risks);
        collectInvalidArrayScalarAssignmentRisks(before, after, risks);
        collectRenamedStandardAnnotationRisks(before, after, risks);
        collectOptionalTypeUseAnnotationLossRisks(before, after, risks);
        collectFunctionalLambdaInvocationRisks(before, after, risks);
        collectMisplacedMethodAnnotationRisks(before, after, risks);
        collectFieldInitializerPrecedenceTextRisks(before, after, risks);
        collectDirectInlineOverloadRisks(before, after, risks);
        collectJavadocConstantExtractionRisks(before, after, risks);
        collectNumericTernaryBoxingRisks(before, after, risks);
        collectUnconditionalReturnBeforeControlRisks(before, after, risks);
        collectSuperHelperInlineRisks(before, after, risks);
        collectDereferenceBeforeNullGuardRisks(before, after, risks);
        collectAssignmentEvaluationOrderRisks(before, after, risks);
        collectStatefulAnonymousExtractionRisks(before, after, risks);
        collectGenericScopeExtractionRisks(before, after, risks);
        collectSynchronizedInlineRisks(before, after, risks);
        collectParallelLambdaSynchronizationLossRisks(before, after, risks);
        collectChangedInlineZeroArgCallRisks(before, after, risks);
        collectBooleanNegationPropagationRisks(before, after, risks);
        collectLoopExitKindChangeRisks(before, after, risks);
        collectVoidReturnValueCallRisks(before, after, risks);
        collectBooleanOutputNegationRisks(before, after, risks);
        collectSelfAssignmentRisks(beforeParsed, afterParsed, risks);
        collectOverrideContractRisks(beforeParsed, afterParsed, risks);
        collectInstanceToStaticDependencyRisks(beforeParsed, afterParsed, risks);
        collectPublicApiShapeRisks(beforeParsed, afterParsed, risks);
        collectLocalToReceiverFieldStateRisks(beforeParsed, afterParsed, risks);
        
        collectStaticMethodNewFieldStateRisks(beforeParsed, afterParsed, risks);
        collectRenameCollisionRisks(beforeParsed, afterParsed, risks);
        collectRepeatedCallCollapseRisks(beforeParsed, afterParsed, risks);
        collectFieldInitializerInlineRisks(beforeParsed, afterParsed, risks);
        collectMissingThisFieldRisks(beforeParsed, afterParsed, risks);
        collectReceiverScopedCallRisks(beforeParsed, afterParsed, risks);
        collectOverloadedInlineLiteralRisks(beforeParsed, afterParsed, risks);
        collectInlineArgumentPrecedenceRisks(beforeParsed, afterParsed, risks);
        collectQualifiedFieldRetargetRisks(beforeParsed, afterParsed, risks);
        collectLocalDispatchShadowRisks(beforeParsed, afterParsed, risks);
        collectIgnoredParameterMutationRisks(beforeParsed, afterParsed, risks);
        collectReflectionStringMismatchRisks(beforeParsed, afterParsed, risks);
        collectInheritedBindingRisks(beforeParsed, afterParsed, risks);
        collectInheritedInlineDispatchRisks(beforeParsed, afterParsed, risks);
        collectLiteralArgumentPropagationRisks(beforeParsed, afterParsed, risks);
        collectOuterMethodBindingRisks(beforeParsed, afterParsed, risks);
        collectUndefinedNewInvocationRisks(beforeParsed, afterParsed, risks);
        collectUnresolvedNameRisks(beforeParsed, afterParsed, risks);
        return new ArrayList<>(risks.values());
    }

    private void collectSelfAssignmentRisks(
            ParsedSnapshot before,
            ParsedSnapshot after,
            Map<String, DeclarationRisk> out) {
        for (MethodFacts method : after.methodsById.values()) {
            int beforeCount = before.methodsById.containsKey(method.id())
                    ? before.methodsById.get(method.id()).selfAssignments().size()
                    : 0;
            if (method.selfAssignments().size() <= beforeCount) {
                continue;
            }
            for (SelfAssignment selfAssignment : method.selfAssignments()) {
                String key = "self:" + method.id() + ":" + selfAssignment.line();
                out.putIfAbsent(key, new DeclarationRisk(
                        method.file(),
                        method.file(),
                        method.container(),
                        method.container(),
                        method.signature(),
                        selfAssignment.line(),
                        selfAssignment.line(),
                        "Suspicious self-assignment or self-initialization detected for `" + selfAssignment.name() + "`."));
            }
        }
    }

    private void collectOverrideContractRisks(
            ParsedSnapshot before,
            ParsedSnapshot after,
            Map<String, DeclarationRisk> out) {
        for (MethodFacts afterMethod : after.methodsById.values()) {
            boolean renamedOverride = afterMethod.overrideAnnotation()
                    && looksLikeRefactoredName(afterMethod.methodName());
            boolean staticOverride = afterMethod.overrideAnnotation() && afterMethod.staticModifier();
            boolean overrideArityChange = afterMethod.overrideAnnotation()
                    && before.containerMethodNameCount(afterMethod.container(), afterMethod.methodName()) == 1
                    && after.containerMethodNameCount(afterMethod.container(), afterMethod.methodName()) == 1
                    && !before.hasMethodInContainer(afterMethod.container(), afterMethod.methodName(), afterMethod.arity());

            if (!renamedOverride && !staticOverride && !overrideArityChange) {
                continue;
            }

            String reason;
            if (renamedOverride) {
                reason = "Annotated override method was renamed to `" + afterMethod.methodName() + "`, breaking the overridden contract.";
            } else if (staticOverride) {
                reason = "Annotated override method `" + afterMethod.methodName() + "(...)` was made static.";
            } else {
                reason = "Annotated override method `" + afterMethod.methodName() + "(...)` changed arity from the original overridden contract.";
            }

            String key = "override-contract:" + afterMethod.id() + ":" + reason;
            out.putIfAbsent(key, new DeclarationRisk(
                    afterMethod.file(),
                    afterMethod.file(),
                    afterMethod.container(),
                    afterMethod.container(),
                    afterMethod.signature(),
                    afterMethod.line(),
                    afterMethod.line(),
                    reason));
        }
    }

    private void collectInstanceToStaticDependencyRisks(
            ParsedSnapshot before,
            ParsedSnapshot after,
            Map<String, DeclarationRisk> out) {
        for (MethodFacts afterMethod : after.methodsById.values()) {
            MethodFacts beforeMethod = before.findEquivalentMethod(afterMethod);
            if (beforeMethod == null || beforeMethod.staticModifier() || !afterMethod.staticModifier()) {
                continue;
            }

            String evidence = staticConversionDependencyEvidence(before, after, beforeMethod, afterMethod);
            if (evidence == null) {
                continue;
            }

            String key = "instance-to-static:" + afterMethod.id();
            out.putIfAbsent(key, new DeclarationRisk(
                    beforeMethod.file(),
                    afterMethod.file(),
                    beforeMethod.container(),
                    afterMethod.container(),
                    afterMethod.signature(),
                    beforeMethod.line(),
                    afterMethod.line(),
                    "Instance method `" + afterMethod.methodName()
                            + "(...)` was changed to static while still depending on " + evidence + "."));
        }
    }

    private String staticConversionDependencyEvidence(
            ParsedSnapshot before,
            ParsedSnapshot after,
            MethodFacts beforeMethod,
            MethodFacts afterMethod) {
        if (afterMethod.usesThisOrSuper()) {
            return "`this` or `super`";
        }

        for (NameUse nameUse : afterMethod.nameUses()) {
            String name = nameUse.name();
            if (afterMethod.inScopeNames().contains(name) || looksLikeExternalReference(name)) {
                continue;
            }
            if (after.hasFieldInContainerChain(afterMethod.container(), name)
                    || beforeMethod.usedNames().contains(name)) {
                return "unqualified instance state `" + name + "`";
            }
        }

        for (InvocationFacts invocation : afterMethod.invocations()) {
            if (invocation.scoped() || invocation.callName().equals(afterMethod.methodName())) {
                continue;
            }
            if (!beforeMethod.invocations().stream().anyMatch(beforeInvocation ->
                    !beforeInvocation.scoped() && beforeInvocation.callName().equals(invocation.callName()))) {
                continue;
            }
            if (after.hasStaticMethodInContainer(afterMethod.container(), invocation.callName(), invocation.argTexts().size())) {
                continue;
            }
            if (looksLikeExternalReference(invocation.callName())) {
                continue;
            }
            return "unqualified instance helper `" + invocation.callName() + "(...)`";
        }

        return null;
    }

    private void collectStaticMethodNewFieldStateRisks(
            ParsedSnapshot before,
            ParsedSnapshot after,
            Map<String, DeclarationRisk> out) {
        for (MethodFacts afterMethod : after.methodsById.values()) {
            MethodFacts beforeMethod = before.findEquivalentMethod(afterMethod);
            if (beforeMethod == null || !afterMethod.staticModifier()) {
                continue;
            }
            for (FieldFacts field : after.fields) {
                if (field.staticModifier() || before.fieldIds.contains(field.id())) {
                    continue;
                }
                if (!field.container().equals(afterMethod.container())) {
                    continue;
                }
                if (!afterMethod.usedNames().contains(field.name())
                        || afterMethod.inScopeNames().contains(field.name())) {
                    continue;
                }
                String key = "static-new-field-state:" + afterMethod.id() + ":" + field.name();
                out.putIfAbsent(key, new DeclarationRisk(
                        beforeMethod.file(),
                        afterMethod.file(),
                        beforeMethod.container(),
                        afterMethod.container(),
                        afterMethod.signature(),
                        beforeMethod.line(),
                        afterMethod.line(),
                        "Static method `" + afterMethod.methodName()
                                + "(...)` now uses newly introduced instance field `" + field.name() + "`."));
            }
        }
    }

    private void collectPublicApiShapeRisks(
            ParsedSnapshot before,
            ParsedSnapshot after,
            Map<String, DeclarationRisk> out) {
        for (MethodFacts beforeMethod : before.methodsById.values()) {
            if (!beforeMethod.publicOrProtected() || isConstructorLike(beforeMethod)) {
                continue;
            }
            if (after.hasMethodInContainer(beforeMethod.container(), beforeMethod.methodName(), beforeMethod.arity())) {
                continue;
            }

            List<MethodFacts> candidates = after.methodsById.values().stream()
                    .filter(candidate -> candidate.publicOrProtected())
                    .filter(candidate -> candidate.container().equals(beforeMethod.container()))
                    .toList();
            for (MethodFacts candidate : candidates) {
                boolean renamedSameArity = candidate.arity() == beforeMethod.arity()
                        && isExplicitRefactoredApiName(beforeMethod.methodName(), candidate.methodName());
                boolean addedParameter = candidate.arity() == beforeMethod.arity() + 1
                        && (candidate.methodName().equals(beforeMethod.methodName())
                                || isExplicitRefactoredApiName(beforeMethod.methodName(), candidate.methodName()));
                if (!renamedSameArity && !addedParameter) {
                    continue;
                }

                String key = "public-api-shape:" + beforeMethod.id() + ":" + candidate.signature();
                String reason = renamedSameArity
                        ? "Public/protected API method `" + beforeMethod.methodName()
                                + "(...)` was renamed to `" + candidate.methodName()
                                + "(...)`, so callers of the original symbol no longer reach the same implementation."
                        : "Public/protected API method `" + beforeMethod.methodName()
                                + "(...)` gained an extra parameter, so callers of the original signature are no longer preserved.";
                out.putIfAbsent(key, new DeclarationRisk(
                        beforeMethod.file(),
                        candidate.file(),
                        beforeMethod.container(),
                        candidate.container(),
                        candidate.signature(),
                        beforeMethod.line(),
                        candidate.line(),
                        reason));
            }
        }
    }

    private void collectLocalToReceiverFieldStateRisks(
            ParsedSnapshot before,
            ParsedSnapshot after,
            Map<String, DeclarationRisk> out) {
        for (FieldFacts field : after.fields) {
            if (field.staticModifier() || before.fieldIds.contains(field.id())) {
                continue;
            }
            if (!before.localNamesByFile.getOrDefault(field.relativePath(), Set.of()).contains(field.name())) {
                continue;
            }

            List<MethodFacts> beforeLocalOwners = before.methodsById.values().stream()
                    .filter(method -> method.relativePath().equals(field.relativePath()))
                    .filter(method -> sameOrNestedContainer(method.container(), field.container()))
                    .filter(method -> method.localNames().contains(field.name()))
                    .toList();
            if (beforeLocalOwners.isEmpty()) {
                continue;
            }

            List<MethodFacts> afterUsers = after.methodsById.values().stream()
                    .filter(method -> method.relativePath().equals(field.relativePath()))
                    .filter(method -> sameOrNestedContainer(method.container(), field.container()))
                    .filter(method -> method.thisAssignedNames().contains(field.name())
                            || (method.assignedNames().contains(field.name()) && !method.localNames().contains(field.name()))
                            || (method.usedNames().contains(field.name()) && !method.localNames().contains(field.name())))
                    .toList();
            if (afterUsers.isEmpty()) {
                continue;
            }

            MethodFacts beforeMethod = beforeLocalOwners.get(0);
            MethodFacts afterMethod = afterUsers.get(0);
            String key = "local-to-field-state:" + field.id();
            out.putIfAbsent(key, new DeclarationRisk(
                    beforeMethod.file(),
                    afterMethod.file(),
                    beforeMethod.container(),
                    afterMethod.container(),
                    field.name(),
                    beforeMethod.line(),
                    Math.max(field.line(), afterMethod.line()),
                    "Method-local variable `" + field.name()
                            + "` was promoted to newly introduced receiver field state, which can change aliasing, visibility, or concurrent behavior."));
        }
    }

    private void collectRelocationRisks(
            ParsedSnapshot before,
            ParsedSnapshot after,
            Map<String, DeclarationRisk> out) {
        for (Map.Entry<String, List<MemberFacts>> entry : before.membersByFingerprint.entrySet()) {
            List<MemberFacts> beforeMembers = entry.getValue();
            List<MemberFacts> afterMembers = after.membersByFingerprint.get(entry.getKey());
            if (afterMembers == null || afterMembers.isEmpty()) {
                continue;
            }

            Set<String> beforeContainers = containers(beforeMembers);
            Set<String> afterContainers = containers(afterMembers);
            if (beforeContainers.equals(afterContainers)) {
                continue;
            }
            Set<String> overlap = new LinkedHashSet<>(beforeContainers);
            overlap.retainAll(afterContainers);
            if (!overlap.isEmpty()) {
                continue;
            }
            if (beforeMembers.get(0) instanceof FieldFacts beforeField
                    && afterMembers.get(0) instanceof FieldFacts afterField
                    && beforeField.name().equals(afterField.name())
                    && after.onlyQualifiedFieldReferences(afterField.name())) {
                continue;
            }

            MemberFacts beforeMember = beforeMembers.get(0);
            MemberFacts afterMember = afterMembers.get(0);
            if (beforeMember instanceof FieldFacts
                    && afterMember instanceof FieldFacts
                    && isSingleTopLevelContainerRename(before, after, beforeMember.container(), afterMember.container())) {
                continue;
            }
            if (beforeMember instanceof MethodFacts beforeMethod
                    && afterMember instanceof MethodFacts afterMethod
                    && isStatelessRunnableExtraction(after, beforeMethod, afterMethod)) {
                continue;
            }
            String key = "move:" + entry.getKey() + ":" + beforeMember.container() + ":" + afterMember.container();
            out.putIfAbsent(key, new DeclarationRisk(
                    beforeMember.file(),
                    afterMember.file(),
                    beforeMember.container(),
                    afterMember.container(),
                    beforeMember.displaySignature(),
                    beforeMember.line(),
                    afterMember.line(),
                    "Declaration moved between containers, which can change binding or dispatch."));
        }
    }

    private boolean isStatelessRunnableExtraction(
            ParsedSnapshot after,
            MethodFacts beforeMethod,
            MethodFacts afterMethod) {
        return beforeMethod.signature().equals("run()")
                && afterMethod.signature().equals("run()")
                && afterMethod.container().startsWith(beforeMethod.container() + ".")
                && after.fields.stream().noneMatch(field -> field.container().equals(afterMethod.container()));
    }

    private boolean isSingleTopLevelContainerRename(
            ParsedSnapshot before,
            ParsedSnapshot after,
            String beforeContainer,
            String afterContainer) {
        return before.topLevelContainers.size() == 1
                && after.topLevelContainers.size() == 1
                && before.topLevelContainers.contains(beforeContainer)
                && after.topLevelContainers.contains(afterContainer)
                && !beforeContainer.equals(afterContainer);
    }

    private void collectFieldCollisionRisks(
            ParsedSnapshot before,
            ParsedSnapshot after,
            Map<String, DeclarationRisk> out) {
        for (FieldFacts field : after.fields) {
            if (before.fieldIds.contains(field.id())) {
                continue;
            }
            if (before.fields.stream().anyMatch(beforeField -> beforeField.name().equals(field.name())
                    && beforeField.initializer().equals(field.initializer())
                    && isSingleTopLevelContainerRename(before, after, beforeField.container(), field.container()))) {
                continue;
            }

            boolean collidesWithField = before.fieldNames.containsKey(field.name())
                    && !before.fieldNames.get(field.name()).contains(field.container());
            boolean collidesWithLocal = after.localNamesByFile.getOrDefault(field.relativePath(), Set.of()).contains(field.name());
            boolean hasQualifiedReference = after.qualifiedNamesByFile
                    .getOrDefault(field.relativePath(), Set.of())
                    .contains(field.name());
            boolean onlyQualifiedUses = after.onlyQualifiedFieldReferences(field.name());
            if ((!collidesWithField && !collidesWithLocal) || hasQualifiedReference || onlyQualifiedUses) {
                continue;
            }

            String key = "field-collision:" + field.id();
            out.putIfAbsent(key, new DeclarationRisk(
                    field.file(),
                    field.file(),
                    field.container(),
                    field.container(),
                    field.name(),
                    field.line(),
                    field.line(),
                    "Added field `" + field.name() + "` now collides with an existing field or local name."));
        }
    }

    private void collectRenameCollisionRisks(
            ParsedSnapshot before,
            ParsedSnapshot after,
            Map<String, DeclarationRisk> out) {
        for (MethodFacts beforeMethod : before.methodsById.values()) {
            for (MethodFacts afterMethod : after.methodsById.values()) {
                if (!beforeMethod.container().equals(afterMethod.container())) {
                    continue;
                }
                if (beforeMethod.arity() != afterMethod.arity()) {
                    continue;
                }
                if (!beforeMethod.bodyFingerprint().equals(afterMethod.bodyFingerprint())) {
                    continue;
                }
                if (beforeMethod.methodName().equals(afterMethod.methodName())) {
                    continue;
                }

                long competingNames = after.methodsById.values().stream()
                        .filter(candidate -> candidate.methodName().equals(afterMethod.methodName()))
                        .count();
                if (competingNames < 2) {
                    continue;
                }
                boolean hasAmbiguousUse = after.methodsById.values().stream()
                        .filter(candidate -> candidate.container().equals(afterMethod.container()))
                        .flatMap(candidate -> candidate.invocations().stream())
                        .filter(invocation -> invocation.callName().equals(afterMethod.methodName()))
                        .filter(invocation -> !invocation.scoped())
                        .filter(invocation -> invocation.argTexts().stream().noneMatch(DeclarationRiskAnalyzer::looksLikeExplicitCast))
                        .anyMatch(invocation -> argumentsCanBindToSignature(invocation.argTexts(), afterMethod.signature()));
                if (!hasAmbiguousUse) {
                    continue;
                }

                String key = "rename-collision:" + beforeMethod.id() + ":" + afterMethod.signature();
                out.putIfAbsent(key, new DeclarationRisk(
                        beforeMethod.file(),
                        afterMethod.file(),
                        beforeMethod.container(),
                        afterMethod.container(),
                        afterMethod.signature(),
                        beforeMethod.line(),
                        afterMethod.line(),
                        "Renamed declaration now collides with another method name in the same snapshot."));
            }
        }
    }

    private void collectRepeatedCallCollapseRisks(
            ParsedSnapshot before,
            ParsedSnapshot after,
            Map<String, DeclarationRisk> out) {
        for (MethodFacts beforeMethod : before.methodsById.values()) {
            MethodFacts afterMethod = after.methodsById.get(beforeMethod.id());
            if (afterMethod == null) {
                continue;
            }

            for (Map.Entry<String, Integer> entry : beforeMethod.callCounts().entrySet()) {
                int beforeCount = entry.getValue();
                int afterCount = afterMethod.callCounts().getOrDefault(entry.getKey(), 0);
                if (beforeCount < 2 || afterCount >= beforeCount) {
                    continue;
                }
                if (afterCount > 0 && entry.getKey().contains(".")) {
                    continue;
                }

                String key = "call-collapse:" + beforeMethod.id() + ":" + entry.getKey();
                out.putIfAbsent(key, new DeclarationRisk(
                        beforeMethod.file(),
                        afterMethod.file(),
                        beforeMethod.container(),
                        afterMethod.container(),
                        beforeMethod.signature(),
                        beforeMethod.line(),
                        afterMethod.line(),
                        "Repeated call `" + entry.getKey() + "` was collapsed, which may suppress side effects."));
            }
        }
    }

    private void collectFieldInitializerInlineRisks(
            ParsedSnapshot before,
            ParsedSnapshot after,
            Map<String, DeclarationRisk> out) {
        for (FieldFacts beforeField : before.fields) {
            FieldFacts afterField = after.findField(beforeField.id());
            if (afterField == null) {
                continue;
            }
            if (beforeField.initializer().equals(afterField.initializer())) {
                continue;
            }
            int open = beforeField.initializer().indexOf('(');
            int close = beforeField.initializer().lastIndexOf(')');
            if (open <= 0 || close <= open) {
                continue;
            }

            String helperName = beforeField.initializer().substring(0, open).trim();
            String arg = beforeField.initializer().substring(open + 1, close).trim();
            if (!containsPrecedenceSensitiveOperator(arg) || arg.startsWith("(")) {
                continue;
            }
            MethodFacts helper = before.findMethodInContainer(helperName, beforeField.container());
            if (helper == null || helper.parameterNames().size() != 1 || helper.returnExpression() == null) {
                continue;
            }
            if (after.methodsById.containsKey(helper.id())) {
                continue;
            }

            String parameter = helper.parameterNames().iterator().next();
            String substituted = normalize(helper.returnExpression()
                    .replaceAll("\\b" + Pattern.quote(parameter) + "\\b", Matcher.quoteReplacement(arg)));
            if (!substituted.equals(afterField.initializer())) {
                continue;
            }
            if (afterField.initializer().contains("(" + arg + ")")) {
                continue;
            }

            String key = "field-inline:" + beforeField.id();
            out.putIfAbsent(key, new DeclarationRisk(
                    beforeField.file(),
                    afterField.file(),
                    beforeField.container(),
                    afterField.container(),
                    beforeField.name(),
                    beforeField.line(),
                    afterField.line(),
                    "Inlining `" + helper.signature() + "` into field initializer `" + beforeField.name()
                            + "` changed operator precedence without parentheses."));
        }
    }

    private void collectLocalDispatchShadowRisks(
            ParsedSnapshot before,
            ParsedSnapshot after,
            Map<String, DeclarationRisk> out) {
        for (MethodFacts afterMethod : after.methodsById.values()) {
            MethodFacts beforeMethod = before.methodsById.get(afterMethod.id());
            if (beforeMethod == null) {
                continue;
            }

            for (InvocationFacts invocation : afterMethod.invocations()) {
                if (invocation.scoped()) {
                    continue;
                }
                if (!beforeMethod.calledNames().contains(invocation.callName())) {
                    continue;
                }

                boolean introducedInSameContainer = after.containerMethodNames
                        .getOrDefault(afterMethod.container(), Set.of())
                        .contains(invocation.callName())
                        && !before.containerMethodNames
                        .getOrDefault(afterMethod.container(), Set.of())
                        .contains(invocation.callName());
                boolean introducedInOuterContainer = after.outerContainerMethodNames(afterMethod.container())
                        .stream()
                        .anyMatch(container -> after.containerMethodNames.getOrDefault(container, Set.of()).contains(invocation.callName())
                                && !before.containerMethodNames.getOrDefault(container, Set.of()).contains(invocation.callName()));
                if (!introducedInSameContainer && !introducedInOuterContainer) {
                    continue;
                }
                if (invocation.argTexts().stream().map(DeclarationRiskAnalyzer::normalize).anyMatch(argument -> argument.contains("new object"))) {
                    continue;
                }
                if (!introducedMethodCanAccept(before, after, afterMethod.container(), invocation)) {
                    continue;
                }

                String key = "dispatch-shadow:" + afterMethod.id() + ":" + invocation.callName() + ":" + invocation.line();
                out.putIfAbsent(key, new DeclarationRisk(
                        beforeMethod.file(),
                        afterMethod.file(),
                        beforeMethod.container(),
                        afterMethod.container(),
                        afterMethod.signature(),
                        beforeMethod.line(),
                        invocation.line(),
                        "Newly introduced method `" + invocation.callName() + "` can change how an unqualified call is bound."));
            }
        }
    }

    private void collectMissingThisFieldRisks(
            ParsedSnapshot before,
            ParsedSnapshot after,
            Map<String, DeclarationRisk> out) {
        for (MethodFacts afterMethod : after.methodsById.values()) {
            MethodFacts beforeMethod = before.methodsById.get(afterMethod.id());
            if (beforeMethod == null) {
                continue;
            }

            for (FieldAccessUse access : afterMethod.thisFieldAccesses()) {
                if (beforeMethod.usedNames().contains(access.name())) {
                    continue;
                }
                if (after.hasFieldInContainerChain(afterMethod.container(), access.name())) {
                    continue;
                }
                if (after.allKnownNames.contains(access.name())) {
                    continue;
                }

                String key = "missing-this-field:" + afterMethod.id() + ":" + access.name() + ":" + access.line();
                out.putIfAbsent(key, new DeclarationRisk(
                        beforeMethod.file(),
                        afterMethod.file(),
                        beforeMethod.container(),
                        afterMethod.container(),
                        afterMethod.signature(),
                        beforeMethod.line(),
                        access.line(),
                        "Qualified field access `this." + access.name() + "` does not resolve in the current container hierarchy."));
            }
        }
    }

    private void collectReceiverScopedCallRisks(
            ParsedSnapshot before,
            ParsedSnapshot after,
            Map<String, DeclarationRisk> out) {
        for (MethodFacts afterMethod : after.methodsById.values()) {
            MethodFacts beforeMethod = before.findEquivalentMethod(afterMethod);
            if (beforeMethod == null) {
                continue;
            }

            for (ScopedCallFacts call : afterMethod.scopedCalls()) {
                String receiverType = afterMethod.variableTypes().get(call.scopeName());
                if (receiverType == null || receiverType.isBlank()) {
                    receiverType = after.fieldTypeInContainerChain(afterMethod.container(), call.scopeName());
                }
                if (receiverType == null || receiverType.isBlank()) {
                    continue;
                }
                Set<String> receiverContainers = after.resolveTypeContainers(receiverType);
                if (receiverContainers.isEmpty()) {
                    continue;
                }
                boolean resolved = receiverContainers.stream()
                        .anyMatch(container -> after.hasMethodInHierarchy(container, call.methodName(), call.arity()));
                if (resolved) {
                    continue;
                }

                String key = "scoped-call:" + afterMethod.id() + ":" + call.scopeName() + ":" + call.methodName() + ":" + call.line();
                out.putIfAbsent(key, new DeclarationRisk(
                        beforeMethod.file(),
                        afterMethod.file(),
                        beforeMethod.container(),
                        afterMethod.container(),
                        afterMethod.signature(),
                        beforeMethod.line(),
                        call.line(),
                        "Scoped call `" + call.scopeName() + "." + call.methodName() + "(...)` does not match any method on receiver type `" + receiverType + "`."));
            }
        }
    }

    private void collectOverloadedInlineLiteralRisks(
            ParsedSnapshot before,
            ParsedSnapshot after,
            Map<String, DeclarationRisk> out) {
        for (MethodFacts beforeCaller : before.methodsById.values()) {
            MethodFacts afterCaller = after.methodsById.get(beforeCaller.id());
            if (afterCaller == null) {
                continue;
            }

            for (InvocationFacts invocation : beforeCaller.invocations()) {
                if (invocation.argTexts().size() != 1) {
                    continue;
                }
                MethodFacts helper = before.findMethodInContainer(invocation.callName(), beforeCaller.container());
                if (helper == null || helper.parameterNames().size() != 0 || helper.returnExpression() == null) {
                    continue;
                }
                if (after.methodsById.containsKey(helper.id())) {
                    continue;
                }
                if (!isLiteralExpression(helper.returnExpression())) {
                    continue;
                }

                for (InvocationFacts outerBefore : beforeCaller.invocations()) {
                    if (outerBefore.argTexts().size() != 1) {
                        continue;
                    }
                    if (!outerBefore.argTexts().get(0).equals(invocation.callName() + "()")) {
                        continue;
                    }
                    for (InvocationFacts outerAfter : afterCaller.invocations()) {
                        if (!outerAfter.callName().equals(outerBefore.callName()) || outerAfter.argTexts().size() != 1) {
                            continue;
                        }
                        String afterArg = normalize(outerAfter.argTexts().get(0));
                        if (!afterArg.equals(helper.returnExpression())) {
                            continue;
                        }
                        if (after.containerMethodArityCount(afterCaller.container(), outerAfter.callName(), 1) < 2) {
                            continue;
                        }
                        if (sameTypeCategory(helper.returnType(), helper.returnExpression())) {
                            continue;
                        }

                        String key = "inline-overload-nested:" + beforeCaller.id() + ":" + outerAfter.callName() + ":" + outerAfter.line();
                        out.putIfAbsent(key, new DeclarationRisk(
                                beforeCaller.file(),
                                afterCaller.file(),
                                beforeCaller.container(),
                                afterCaller.container(),
                                afterCaller.signature(),
                                beforeCaller.line(),
                                outerAfter.line(),
                                "Inlining `" + helper.signature() + "` into overloaded call `" + outerAfter.callName() + "(...)` changed the apparent argument type."));
                    }
                }

                for (InvocationFacts afterInvocation : afterCaller.invocations()) {
                    if (after.containerMethodArityCount(afterCaller.container(), afterInvocation.callName(), 1) < 2) {
                        continue;
                    }
                    String afterArg = normalize(afterInvocation.argTexts().get(0));
                    if (!afterArg.equals(helper.returnExpression())) {
                        continue;
                    }
                    if (sameTypeCategory(helper.returnType(), helper.returnExpression())) {
                        continue;
                    }

                    String key = "inline-overload:" + beforeCaller.id() + ":" + afterInvocation.callName() + ":" + afterInvocation.line();
                    out.putIfAbsent(key, new DeclarationRisk(
                            beforeCaller.file(),
                            afterCaller.file(),
                            beforeCaller.container(),
                            afterCaller.container(),
                            afterCaller.signature(),
                            beforeCaller.line(),
                            afterInvocation.line(),
                            "Inlining `" + helper.signature() + "` changed the apparent argument type at overloaded call `" + afterInvocation.callName() + "(...)`."));
                }
            }
        }
    }

    private void collectInlineArgumentPrecedenceRisks(
            ParsedSnapshot before,
            ParsedSnapshot after,
            Map<String, DeclarationRisk> out) {
        for (MethodFacts beforeCaller : before.methodsById.values()) {
            MethodFacts afterCaller = after.methodsById.get(beforeCaller.id());
            if (afterCaller == null) {
                continue;
            }

            for (InvocationFacts invocation : beforeCaller.invocations()) {
                MethodFacts helper = before.findMethodInContainer(invocation.callName(), beforeCaller.container());
                if (helper == null || helper.parameterNames().isEmpty()) {
                    continue;
                }
                if (after.methodsById.containsKey(helper.id())) {
                    continue;
                }
                if (!containsPrecedenceSensitiveOperator(helper.bodyFingerprint())) {
                    continue;
                }

                for (String argText : invocation.argTexts()) {
                    String normalizedArg = normalize(argText);
                    if (!containsPrecedenceSensitiveOperator(normalizedArg) || normalizedArg.startsWith("(")) {
                        continue;
                    }
                    if (!afterCaller.bodyFingerprint().contains(normalizedArg)) {
                        continue;
                    }
                    if (afterCaller.bodyFingerprint().contains("(" + normalizedArg + ")")) {
                        continue;
                    }

                    String key = "inline-precedence:" + beforeCaller.id() + ":" + invocation.callName() + ":" + normalizedArg;
                    out.putIfAbsent(key, new DeclarationRisk(
                            beforeCaller.file(),
                            afterCaller.file(),
                            beforeCaller.container(),
                            afterCaller.container(),
                            afterCaller.signature(),
                            beforeCaller.line(),
                            afterCaller.line(),
                            "Inlining argument `" + normalizedArg + "` into `" + invocation.callName() + "(...)` may change evaluation precedence without parentheses."));
                }
            }
        }
    }

    private void collectQualifiedFieldRetargetRisks(
            ParsedSnapshot before,
            ParsedSnapshot after,
            Map<String, DeclarationRisk> out) {
        for (MethodFacts afterMethod : after.methodsById.values()) {
            MethodFacts beforeMethod = before.methodsById.get(afterMethod.id());
            if (beforeMethod == null) {
                continue;
            }

            for (QualifiedFieldRef beforeRef : beforeMethod.qualifiedFieldRefs()) {
                for (QualifiedFieldRef afterRef : afterMethod.qualifiedFieldRefs()) {
                    if (!beforeRef.fieldName().equals(afterRef.fieldName())) {
                        continue;
                    }
                    if (beforeRef.scopeName().equals(afterRef.scopeName())) {
                        continue;
                    }

                    Set<String> beforeContainers = after.resolveTypeContainers(normalize(beforeRef.scopeName()));
                    Set<String> afterContainers = after.resolveTypeContainers(normalize(afterRef.scopeName()));
                    if (beforeContainers.isEmpty() || afterContainers.isEmpty()) {
                        continue;
                    }
                    boolean oldStillDefines = beforeContainers.stream()
                            .anyMatch(container -> after.containerFieldNames.getOrDefault(container, Set.of()).contains(beforeRef.fieldName()));
                    boolean newDefines = afterContainers.stream()
                            .anyMatch(container -> after.containerFieldNames.getOrDefault(container, Set.of()).contains(afterRef.fieldName()));
                    if (!oldStillDefines || !newDefines) {
                        continue;
                    }

                    String key = "qualified-retarget:" + afterMethod.id() + ":" + beforeRef.fieldName() + ":" + beforeRef.scopeName() + ":" + afterRef.scopeName();
                    out.putIfAbsent(key, new DeclarationRisk(
                            beforeMethod.file(),
                            afterMethod.file(),
                            beforeMethod.container(),
                            afterMethod.container(),
                            afterMethod.signature(),
                            beforeMethod.line(),
                            afterRef.line(),
                            "Qualified field access changed from `" + beforeRef.scopeName() + "." + beforeRef.fieldName()
                                    + "` to `" + afterRef.scopeName() + "." + afterRef.fieldName() + "`, which can retarget behavior."));
                }
            }
        }
    }

    private void collectInheritedInlineDispatchRisks(
            ParsedSnapshot before,
            ParsedSnapshot after,
            Map<String, DeclarationRisk> out) {
        for (MethodFacts beforeCaller : before.methodsById.values()) {
            MethodFacts afterCaller = after.methodsById.get(beforeCaller.id());
            if (afterCaller == null) {
                continue;
            }

            for (InvocationFacts invocation : beforeCaller.invocations()) {
                MethodFacts helper = before.findInheritedMethod(beforeCaller.container(), invocation.callName(), invocation.argTexts().size());
                if (helper == null || after.methodsById.containsKey(helper.id())) {
                    continue;
                }
                if (helper.invocations().size() != 1) {
                    continue;
                }
                InvocationFacts nested = helper.invocations().get(0);
                boolean inlinedIntoCaller = afterCaller.invocations().stream()
                        .anyMatch(candidate -> candidate.callName().equals(nested.callName())
                                && candidate.argTexts().equals(nested.argTexts()));
                if (!inlinedIntoCaller) {
                    continue;
                }
                if (after.findMethodInContainer(nested.callName(), afterCaller.container()) == null) {
                    continue;
                }

                String key = "inherited-inline:" + beforeCaller.id() + ":" + nested.callName() + ":" + afterCaller.line();
                out.putIfAbsent(key, new DeclarationRisk(
                        beforeCaller.file(),
                        afterCaller.file(),
                        beforeCaller.container(),
                        afterCaller.container(),
                        afterCaller.signature(),
                        beforeCaller.line(),
                        afterCaller.line(),
                        "Inlining inherited helper `" + helper.signature() + "` exposed unqualified call `" + nested.callName() + "(...)` to subclass dispatch."));
            }
        }
    }

    private void collectLiteralArgumentPropagationRisks(
            ParsedSnapshot before,
            ParsedSnapshot after,
            Map<String, DeclarationRisk> out) {
        for (MethodFacts afterMethod : after.methodsById.values()) {
            if (afterMethod.parameterNames().isEmpty()) {
                continue;
            }

            MethodFacts beforeMethod = before.findMethodInContainer(afterMethod.methodName(), afterMethod.container());
            if (beforeMethod == null) {
                continue;
            }

            for (InvocationFacts invocation : afterMethod.invocations()) {
                boolean hasLiteralArgument = invocation.argTexts().stream().anyMatch(DeclarationRiskAnalyzer::isLiteralExpression);
                if (!hasLiteralArgument) {
                    continue;
                }
                if (!beforeMethod.bodyFingerprint().contains(normalize(invocation.callName() + "()"))) {
                    continue;
                }
                boolean parameterStillPropagated = afterMethod.invocations().stream()
                        .filter(candidate -> candidate.callName().equals(invocation.callName()))
                        .flatMap(candidate -> candidate.argTexts().stream())
                        .anyMatch(afterMethod.parameterNames()::contains);
                if (!parameterStillPropagated) {
                    continue;
                }

                String key = "literal-propagation:" + afterMethod.id() + ":" + invocation.callName() + ":" + invocation.line();
                out.putIfAbsent(key, new DeclarationRisk(
                        beforeMethod.file(),
                        afterMethod.file(),
                        beforeMethod.container(),
                        afterMethod.container(),
                        afterMethod.signature(),
                        beforeMethod.line(),
                        invocation.line(),
                        "Parameter propagation into `" + invocation.callName() + "(...)` was replaced with a literal argument."));
            }
        }
    }

    private void collectUnresolvedNameRisks(
            ParsedSnapshot before,
            ParsedSnapshot after,
            Map<String, DeclarationRisk> out) {
        for (MethodFacts afterMethod : after.methodsById.values()) {
            MethodFacts beforeMethod = before.methodsById.get(afterMethod.id());
            if (beforeMethod == null) {
                continue;
            }

            for (NameUse unresolved : afterMethod.nameUses()) {
                if (beforeMethod.usedNames().contains(unresolved.name())) {
                    continue;
                }
                if (afterMethod.inScopeNames().contains(unresolved.name())) {
                    continue;
                }
                if (after.allKnownNames.contains(unresolved.name())) {
                    continue;
                }
                if (after.allKnownNames.stream().anyMatch(known -> known.equalsIgnoreCase(unresolved.name()))) {
                    continue;
                }
                if (looksLikeExternalReference(unresolved.name())) {
                    continue;
                }

                String key = "unresolved-name:" + afterMethod.id() + ":" + unresolved.name() + ":" + unresolved.line();
                out.putIfAbsent(key, new DeclarationRisk(
                        beforeMethod.file(),
                        afterMethod.file(),
                        beforeMethod.container(),
                        afterMethod.container(),
                        afterMethod.signature(),
                        beforeMethod.line(),
                        unresolved.line(),
                        "Name `" + unresolved.name() + "` is newly introduced but not declared in the current snapshot."));
            }
        }
    }

    private void collectUndefinedNewInvocationRisks(
            ParsedSnapshot before,
            ParsedSnapshot after,
            Map<String, DeclarationRisk> out) {
        for (MethodFacts afterMethod : after.methodsById.values()) {
            MethodFacts beforeMethod = before.findEquivalentMethod(afterMethod);
            if (beforeMethod == null) {
                continue;
            }

            for (InvocationFacts invocation : afterMethod.invocations()) {
                if (invocation.scoped()
                        || beforeMethod.invocations().stream().anyMatch(beforeInvocation ->
                                !beforeInvocation.scoped() && beforeInvocation.callName().equals(invocation.callName()))
                        || before.hasInvocationNamed(invocation.callName())
                        || isKnownObjectMethod(invocation.callName())) {
                    continue;
                }
                if (looksLikeExternalReference(invocation.callName())) {
                    continue;
                }
                if (after.allMethodNames.contains(invocation.callName())) {
                    continue;
                }
                if (after.hasMethodInHierarchy(afterMethod.container(), invocation.callName(), invocation.argTexts().size())) {
                    continue;
                }
                if (!looksLikeGeneratedUndefinedCall(invocation.callName(), before, after)) {
                    continue;
                }

                String key = "undefined-new-call:" + afterMethod.id() + ":" + invocation.callName() + ":" + invocation.line();
                out.putIfAbsent(key, new DeclarationRisk(
                        beforeMethod.file(),
                        afterMethod.file(),
                        beforeMethod.container(),
                        afterMethod.container(),
                        afterMethod.signature(),
                        beforeMethod.line(),
                        invocation.line(),
                        "Newly introduced call `" + invocation.callName()
                                + "(...)` does not resolve to any method in the refactored snapshot."));
            }
        }
    }

    private boolean looksLikeGeneratedUndefinedCall(String callName, ParsedSnapshot before, ParsedSnapshot after) {
        String normalizedCall = normalize(callName);
        if (looksLikeRefactoredName(callName)) {
            return true;
        }
        Set<String> knownMethodNames = new LinkedHashSet<>();
        knownMethodNames.addAll(before.allMethodNames);
        knownMethodNames.addAll(after.allMethodNames);
        for (String known : knownMethodNames) {
            String normalizedKnown = normalize(known);
            if (normalizedKnown.isBlank() || normalizedKnown.equals(normalizedCall)) {
                continue;
            }
            if (normalizedCall.startsWith(normalizedKnown)
                    && normalizedCall.length() >= normalizedKnown.length() + 4) {
                return true;
            }
        }
        return false;
    }

    private boolean looksLikeExternalReference(String name) {
        return name.isBlank()
                || Character.isUpperCase(name.charAt(0))
                || name.equals("java");
    }

    private boolean isKnownObjectMethod(String name) {
        return name.equals("wait")
                || name.equals("notify")
                || name.equals("notifyAll")
                || name.equals("equals")
                || name.equals("hashCode")
                || name.equals("toString")
                || name.equals("getClass");
    }

    private boolean looksLikeRefactoredName(String name) {
        String normalized = normalize(name);
        return normalized.contains("refactor")
                || normalized.contains("_mini")
                || normalized.contains("generated");
    }

    private boolean introducedMethodCanAccept(
            ParsedSnapshot before,
            ParsedSnapshot after,
            String callerContainer,
            InvocationFacts invocation) {
        Set<String> visibleContainers = new LinkedHashSet<>();
        visibleContainers.add(callerContainer);
        visibleContainers.addAll(after.outerContainerMethodNames(callerContainer));

        return after.methodsById.values().stream()
                .filter(candidate -> visibleContainers.contains(candidate.container()))
                .filter(candidate -> candidate.methodName().equals(invocation.callName()))
                .filter(candidate -> candidate.arity() == invocation.argTexts().size())
                .filter(candidate -> !before.hasMethodInContainer(candidate.container(), candidate.methodName(), candidate.arity()))
                .anyMatch(candidate -> argumentsCanBindToSignature(invocation.argTexts(), candidate.signature()));
    }

    private void collectIgnoredParameterMutationRisks(
            ParsedSnapshot before,
            ParsedSnapshot after,
            Map<String, DeclarationRisk> out) {
        for (MethodFacts caller : after.methodsById.values()) {
            MethodFacts beforeCaller = before.methodsById.get(caller.id());
            if (beforeCaller == null) {
                continue;
            }

            for (CallSiteFacts callSite : caller.expressionStatementCalls()) {
                MethodFacts callee = after.findMethodInContainer(callSite.name(), caller.container());
                if (callee == null || !callee.mutatedParameters()) {
                    continue;
                }
                if (!callee.returnsVoid()) {
                    continue;
                }
                if (before.methodsById.containsKey(callee.id())) {
                    continue;
                }

                String key = "ignored-mutation:" + caller.id() + ":" + callSite.name() + ":" + callSite.line();
                out.putIfAbsent(key, new DeclarationRisk(
                        beforeCaller.file(),
                        caller.file(),
                        beforeCaller.container(),
                        caller.container(),
                        caller.signature(),
                        beforeCaller.line(),
                        callSite.line(),
                        "Extracted helper `" + callSite.name() + "` mutates parameters, but the caller ignores any propagated state."));
            }
        }
    }

    private void collectOuterMethodBindingRisks(
            ParsedSnapshot before,
            ParsedSnapshot after,
            Map<String, DeclarationRisk> out) {
        for (MethodFacts afterMethod : after.methodsById.values()) {
            if (!afterMethod.container().contains(".")) {
                continue;
            }

            for (CallSiteFacts callSite : afterMethod.unqualifiedCalls()) {
                boolean introducedInOuter = after.outerContainerMethodNames(afterMethod.container()).stream()
                        .anyMatch(container -> after.containerMethodNames.getOrDefault(container, Set.of()).contains(callSite.name())
                                && !before.containerMethodNames.getOrDefault(container, Set.of()).contains(callSite.name()));
                if (!introducedInOuter) {
                    continue;
                }

                String key = "outer-bind:" + afterMethod.id() + ":" + callSite.name() + ":" + callSite.line();
                out.putIfAbsent(key, new DeclarationRisk(
                        afterMethod.file(),
                        afterMethod.file(),
                        afterMethod.container(),
                        afterMethod.container(),
                        afterMethod.signature(),
                        afterMethod.line(),
                        callSite.line(),
                        "Nested method now calls a newly introduced outer method `" + callSite.name() + "`, which can change binding."));
            }
        }
    }

    private void collectInheritedBindingRisks(
            ParsedSnapshot before,
            ParsedSnapshot after,
            Map<String, DeclarationRisk> out) {
        for (MethodFacts afterMethod : after.methodsById.values()) {
            MethodFacts beforeMethod = before.methodsById.get(afterMethod.id());
            if (beforeMethod == null) {
                continue;
            }

            for (CallSiteFacts callSite : afterMethod.unqualifiedCalls()) {
                if (!beforeMethod.calledNames().contains(callSite.name())) {
                    continue;
                }
                boolean visibleOnOuterBefore = before.outerContainerMethodNames(beforeMethod.container()).stream()
                        .anyMatch(container -> before.containerMethodNames.getOrDefault(container, Set.of()).contains(callSite.name()));
                if (!visibleOnOuterBefore) {
                    continue;
                }
                boolean introducedOnAncestor = after.ancestorContainers(afterMethod.container()).stream()
                        .anyMatch(container -> after.containerMethodNames.getOrDefault(container, Set.of()).contains(callSite.name())
                                && !before.containerMethodNames.getOrDefault(container, Set.of()).contains(callSite.name()));
                if (!introducedOnAncestor) {
                    continue;
                }

                String key = "inherited-bind:" + afterMethod.id() + ":" + callSite.name() + ":" + callSite.line();
                out.putIfAbsent(key, new DeclarationRisk(
                        beforeMethod.file(),
                        afterMethod.file(),
                        beforeMethod.container(),
                        afterMethod.container(),
                        afterMethod.signature(),
                        beforeMethod.line(),
                        callSite.line(),
                        "New ancestor method `" + callSite.name() + "` can change how an unqualified call is bound."));
            }
        }
    }

    private void collectReflectionStringMismatchRisks(
            ParsedSnapshot before,
            ParsedSnapshot after,
            Map<String, DeclarationRisk> out) {
        for (MethodFacts caller : after.methodsById.values()) {
            MethodFacts beforeCaller = before.methodsById.get(caller.id());
            if (beforeCaller == null) {
                continue;
            }

            for (ReflectionLookup lookup : caller.reflectionLookups()) {
                if (after.allMethodNames.contains(lookup.methodName())) {
                    continue;
                }
                if (!before.allMethodNames.contains(lookup.methodName())) {
                    continue;
                }

                String key = "reflection:" + caller.id() + ":" + lookup.methodName() + ":" + lookup.line();
                out.putIfAbsent(key, new DeclarationRisk(
                        beforeCaller.file(),
                        caller.file(),
                        beforeCaller.container(),
                        caller.container(),
                        caller.signature(),
                        beforeCaller.line(),
                        lookup.line(),
                        "Reflection lookup still references `" + lookup.methodName() + "`, but that declaration is no longer present."));
            }
        }
    }

    private Set<String> containers(Collection<MemberFacts> members) {
        Set<String> containers = new LinkedHashSet<>();
        for (MemberFacts member : members) {
            containers.add(member.container());
        }
        return containers;
    }

    public record DeclarationRisk(
            String beforeFile,
            String afterFile,
            String beforeContainer,
            String afterContainer,
            String signature,
            int beforeLine,
            int afterLine,
            String reason) {
    }

    private static final class ParsedSnapshot {
        private final List<FieldFacts> fields;
        private final Map<String, MethodFacts> methodsById;
        private final Map<String, List<MemberFacts>> membersByFingerprint;
        private final Map<String, Set<String>> fieldNames;
        private final Set<String> fieldIds;
        private final Map<String, Set<String>> localNamesByFile;
        private final Map<String, Set<String>> qualifiedNamesByFile;
        private final Map<String, Set<String>> containerFieldNames;
        private final Map<String, Set<String>> containerMethodNames;
        private final Map<String, Set<String>> superTypeNamesByContainer;
        private final Map<String, Set<String>> simpleTypeToContainers;
        private final Set<String> allMethodNames;
        private final Set<String> allKnownNames;
        private final Set<String> topLevelContainers;

        private ParsedSnapshot(
                List<FieldFacts> fields,
                Map<String, MethodFacts> methodsById,
                Map<String, List<MemberFacts>> membersByFingerprint,
                Map<String, Set<String>> fieldNames,
                Set<String> fieldIds,
                Map<String, Set<String>> localNamesByFile,
                Map<String, Set<String>> qualifiedNamesByFile,
                Map<String, Set<String>> containerFieldNames,
                Map<String, Set<String>> containerMethodNames,
                Map<String, Set<String>> superTypeNamesByContainer,
                Map<String, Set<String>> simpleTypeToContainers,
                Set<String> allMethodNames,
                Set<String> allKnownNames,
                Set<String> topLevelContainers) {
            this.fields = fields;
            this.methodsById = methodsById;
            this.membersByFingerprint = membersByFingerprint;
            this.fieldNames = fieldNames;
            this.fieldIds = fieldIds;
            this.localNamesByFile = localNamesByFile;
            this.qualifiedNamesByFile = qualifiedNamesByFile;
            this.containerFieldNames = containerFieldNames;
            this.containerMethodNames = containerMethodNames;
            this.superTypeNamesByContainer = superTypeNamesByContainer;
            this.simpleTypeToContainers = simpleTypeToContainers;
            this.allMethodNames = allMethodNames;
            this.allKnownNames = allKnownNames;
            this.topLevelContainers = topLevelContainers;
        }

        private static ParsedSnapshot from(SourceSnapshot snapshot) {
            List<FieldFacts> fields = new ArrayList<>();
            Map<String, MethodFacts> methodsById = new LinkedHashMap<>();
            Map<String, List<MemberFacts>> membersByFingerprint = new LinkedHashMap<>();
            Map<String, Set<String>> fieldNames = new HashMap<>();
            Set<String> fieldIds = new HashSet<>();
            Map<String, Set<String>> localNamesByFile = new HashMap<>();
            Map<String, Set<String>> qualifiedNamesByFile = new HashMap<>();
            Map<String, Set<String>> containerFieldNames = new HashMap<>();
            Map<String, Set<String>> containerMethodNames = new HashMap<>();
            Map<String, Set<String>> superTypeNamesByContainer = new HashMap<>();
            Map<String, Set<String>> simpleTypeToContainers = new HashMap<>();
            Set<String> allMethodNames = new LinkedHashSet<>();
            Set<String> allKnownNames = new LinkedHashSet<>();
            Set<String> topLevelContainers = new LinkedHashSet<>();

            for (SourceFileUnit file : snapshot.files()) {
                parseCompilationUnit(file.content()).ifPresent(cu -> {
                    for (TypeDeclaration<?> typeDeclaration : cu.findAll(TypeDeclaration.class)) {
                        String container = resolveContainer(typeDeclaration, cu);
                        if (typeDeclaration.getParentNode().filter(TypeDeclaration.class::isInstance).isEmpty()) {
                            topLevelContainers.add(container);
                        }
                        simpleTypeToContainers
                                .computeIfAbsent(normalize(typeDeclaration.getNameAsString()), unused -> new LinkedHashSet<>())
                                .add(container);
                        if (typeDeclaration instanceof com.github.javaparser.ast.body.ClassOrInterfaceDeclaration classOrInterface) {
                            for (var extendedType : classOrInterface.getExtendedTypes()) {
                                superTypeNamesByContainer
                                        .computeIfAbsent(container, unused -> new LinkedHashSet<>())
                                        .add(normalize(extendedType.getNameAsString()));
                            }
                        }
                    }

                    cu.findAll(com.github.javaparser.ast.expr.FieldAccessExpr.class).forEach(access -> {
                        if (access.getScope().isThisExpr() || access.getScope().toString().contains(".this")) {
                            qualifiedNamesByFile
                                    .computeIfAbsent(file.relativePath(), unused -> new LinkedHashSet<>())
                                    .add(access.getNameAsString());
                        }
                    });

                    for (FieldDeclaration field : cu.findAll(FieldDeclaration.class)) {
                        String container = resolveContainer(field, cu);
                        for (VariableDeclarator variable : field.getVariables()) {
                            String initializer = variable.getInitializer()
                                    .map(Expression::toString)
                                    .map(DeclarationRiskAnalyzer::normalize)
                                    .orElse("");
                            int line = variable.getRange().map(r -> r.begin.line).orElse(-1);
                            FieldFacts facts = new FieldFacts(
                                    file.absolutePath().toString(),
                                    file.relativePath(),
                                    container,
                                    variable.getNameAsString(),
                                    normalize(variable.getType().asString()),
                                    field.getModifiers().stream()
                                            .anyMatch(modifier -> modifier.getKeyword() == Modifier.Keyword.STATIC),
                                    initializer,
                                    line);
                            fields.add(facts);
                            fieldIds.add(facts.id());
                            fieldNames.computeIfAbsent(facts.name(), unused -> new LinkedHashSet<>()).add(facts.container());
                            containerFieldNames.computeIfAbsent(facts.container(), unused -> new LinkedHashSet<>()).add(facts.name());
                            membersByFingerprint.computeIfAbsent(facts.fingerprint(), unused -> new ArrayList<>()).add(facts);
                            allKnownNames.add(facts.name());
                        }
                    }

                    for (MethodDeclaration method : cu.findAll(MethodDeclaration.class)) {
                        method.getBody().ifPresent(body -> {
                            MethodFacts facts = toMethodFacts(file, cu, method, body);
                            methodsById.put(facts.id(), facts);
                            membersByFingerprint.computeIfAbsent(facts.fingerprint(), unused -> new ArrayList<>()).add(facts);
                            localNamesByFile.computeIfAbsent(file.relativePath(), unused -> new LinkedHashSet<>()).addAll(facts.localNames());
                            containerMethodNames.computeIfAbsent(facts.container(), unused -> new LinkedHashSet<>()).add(facts.methodName());
                            allMethodNames.add(facts.methodName());
                            allKnownNames.add(facts.methodName());
                            allKnownNames.addAll(facts.inScopeNames());
                        });
                    }
                    for (ConstructorDeclaration constructor : cu.findAll(ConstructorDeclaration.class)) {
                        MethodFacts facts = toMethodFacts(file, cu, constructor, constructor.getBody());
                        methodsById.put(facts.id(), facts);
                        membersByFingerprint.computeIfAbsent(facts.fingerprint(), unused -> new ArrayList<>()).add(facts);
                        localNamesByFile.computeIfAbsent(file.relativePath(), unused -> new LinkedHashSet<>()).addAll(facts.localNames());
                        containerMethodNames.computeIfAbsent(facts.container(), unused -> new LinkedHashSet<>()).add(facts.methodName());
                        allMethodNames.add(facts.methodName());
                        allKnownNames.add(facts.methodName());
                        allKnownNames.addAll(facts.inScopeNames());
                    }
                });
            }

            return new ParsedSnapshot(
                    fields,
                    methodsById,
                    membersByFingerprint,
                    fieldNames,
                    fieldIds,
                    localNamesByFile,
                    qualifiedNamesByFile,
                    containerFieldNames,
                    containerMethodNames,
                    superTypeNamesByContainer,
                    simpleTypeToContainers,
                    allMethodNames,
                    allKnownNames,
                    topLevelContainers);
        }

        private Set<String> outerContainerMethodNames(String container) {
            Set<String> outers = new LinkedHashSet<>();
            int idx = container.lastIndexOf('.');
            while (idx > 0) {
                String candidate = container.substring(0, idx);
                if (containerMethodNames.containsKey(candidate)) {
                    outers.add(candidate);
                }
                idx = candidate.lastIndexOf('.');
            }
            return outers;
        }

        private Set<String> ancestorContainers(String container) {
            Set<String> ancestors = new LinkedHashSet<>();
            collectAncestors(container, ancestors, new HashSet<>());
            return ancestors;
        }

        private void collectAncestors(String container, Set<String> out, Set<String> seen) {
            if (!seen.add(container)) {
                return;
            }
            for (String superType : superTypeNamesByContainer.getOrDefault(container, Set.of())) {
                for (String resolved : resolveTypeContainers(superType)) {
                    if (out.add(resolved)) {
                        collectAncestors(resolved, out, seen);
                    }
                }
            }
        }

        private Set<String> resolveTypeContainers(String normalizedTypeName) {
            Set<String> direct = simpleTypeToContainers.get(normalizedTypeName);
            if (direct != null && !direct.isEmpty()) {
                return direct;
            }
            Set<String> bySuffix = new LinkedHashSet<>();
            for (String container : containerMethodNames.keySet()) {
                if (normalize(simpleName(container)).equals(normalizedTypeName)) {
                    bySuffix.add(container);
                }
            }
            return bySuffix;
        }

        private boolean hasMethodInHierarchy(String container, String methodName, int arity) {
            if (hasMethodInContainer(container, methodName, arity)) {
                return true;
            }
            for (String ancestor : ancestorContainers(container)) {
                if (hasMethodInContainer(ancestor, methodName, arity)) {
                    return true;
                }
            }
            return false;
        }

        private boolean hasMethodInContainer(String container, String methodName, int arity) {
            return methodsById.values().stream()
                    .filter(method -> method.container().equals(container))
                    .anyMatch(method -> method.methodName().equals(methodName) && method.arity() == arity);
        }

        private int containerMethodArityCount(String container, String methodName, int arity) {
            return (int) methodsById.values().stream()
                    .filter(method -> method.container().equals(container))
                    .filter(method -> method.methodName().equals(methodName) && method.arity() == arity)
                    .count();
        }

        private int containerMethodNameCount(String container, String methodName) {
            return (int) methodsById.values().stream()
                    .filter(method -> method.container().equals(container))
                    .filter(method -> method.methodName().equals(methodName))
                    .count();
        }

        private boolean hasFieldInContainerChain(String container, String fieldName) {
            if (containerFieldNames.getOrDefault(container, Set.of()).contains(fieldName)) {
                return true;
            }
            int idx = container.lastIndexOf('.');
            while (idx > 0) {
                container = container.substring(0, idx);
                if (containerFieldNames.getOrDefault(container, Set.of()).contains(fieldName)) {
                    return true;
                }
                idx = container.lastIndexOf('.');
            }
            return false;
        }

        private boolean onlyQualifiedFieldReferences(String fieldName) {
            boolean seenQualified = false;
            for (MethodFacts method : methodsById.values()) {
                if (method.usedNames().contains(fieldName)) {
                    return false;
                }
                if (method.usesFieldOnlyQualified(fieldName)) {
                    seenQualified = true;
                }
            }
            return seenQualified;
        }

        private FieldFacts findField(String id) {
            for (FieldFacts field : fields) {
                if (field.id().equals(id)) {
                    return field;
                }
            }
            return null;
        }

        private boolean hasField(String container, String fieldName) {
            return containerFieldNames.getOrDefault(container, Set.of()).contains(fieldName);
        }

        private MethodFacts findEquivalentMethod(MethodFacts method) {
            MethodFacts exact = methodsById.get(method.id());
            if (exact != null) {
                return exact;
            }
            List<MethodFacts> signatureMatches = methodsById.values().stream()
                    .filter(candidate -> candidate.container().equals(method.container()))
                    .filter(candidate -> candidate.signature().equals(method.signature()))
                    .toList();
            if (signatureMatches.size() == 1) {
                return signatureMatches.get(0);
            }
            List<MethodFacts> arityMatches = methodsById.values().stream()
                    .filter(candidate -> candidate.container().equals(method.container()))
                    .filter(candidate -> candidate.methodName().equals(method.methodName()))
                    .filter(candidate -> candidate.arity() == method.arity())
                    .toList();
            return arityMatches.size() == 1 ? arityMatches.get(0) : null;
        }

        private boolean hasStaticMethodInContainer(String container, String methodName, int arity) {
            MethodFacts method = findMethodInContainer(methodName, container, arity);
            return method != null && method.staticModifier();
        }

        private boolean hasInvocationNamed(String methodName) {
            return methodsById.values().stream()
                    .flatMap(method -> method.invocations().stream())
                    .anyMatch(invocation -> !invocation.scoped() && invocation.callName().equals(methodName));
        }

        private String fieldTypeInContainerChain(String container, String fieldName) {
            String direct = fieldTypeInContainer(container, fieldName);
            if (direct != null) {
                return direct;
            }
            int idx = container.lastIndexOf('.');
            while (idx > 0) {
                container = container.substring(0, idx);
                String inherited = fieldTypeInContainer(container, fieldName);
                if (inherited != null) {
                    return inherited;
                }
                idx = container.lastIndexOf('.');
            }
            return null;
        }

        private String fieldTypeInContainer(String container, String fieldName) {
            return fields.stream()
                    .filter(field -> field.container().equals(container))
                    .filter(field -> field.name().equals(fieldName))
                    .map(FieldFacts::type)
                    .findFirst()
                    .orElse(null);
        }

        private MethodFacts findInheritedMethod(String container, String methodName, int arity) {
            for (String ancestor : ancestorContainers(container)) {
                MethodFacts found = findMethodInContainer(methodName, ancestor, arity);
                if (found != null) {
                    return found;
                }
            }
            return null;
        }

        private MethodFacts findMethodInContainer(String methodName, String container) {
            return methodsById.values().stream()
                    .filter(method -> method.container().equals(container))
                    .filter(method -> method.methodName().equals(methodName))
                    .findFirst()
                    .orElse(null);
        }

        private MethodFacts findMethodInContainer(String methodName, String container, int arity) {
            return methodsById.values().stream()
                    .filter(method -> method.container().equals(container))
                    .filter(method -> method.methodName().equals(methodName))
                    .filter(method -> method.arity() == arity)
                    .findFirst()
                    .orElse(null);
        }

        private static MethodFacts toMethodFacts(
                SourceFileUnit file,
                CompilationUnit cu,
                CallableDeclaration<?> callable,
                BlockStmt body) {
            String container = resolveContainer(callable, cu);
            String signature = signature(callable);
            String methodName = callable.getNameAsString();
            int arity = callable.getParameters().size();
            int line = callable.getRange().map(r -> r.begin.line).orElse(-1);
            String id = file.relativePath() + "::" + container + "::" + signature;

            List<SelfAssignment> selfAssignments = new ArrayList<>();
            Set<String> assignedNames = new LinkedHashSet<>();
            Set<String> thisAssignedNames = new LinkedHashSet<>();
            body.findAll(AssignExpr.class).forEach(assign -> {
                Expression target = assign.getTarget();
                if (target.isNameExpr()) {
                    assignedNames.add(target.asNameExpr().getNameAsString());
                } else if (target.isFieldAccessExpr()) {
                    var fieldAccess = target.asFieldAccessExpr();
                    if (fieldAccess.getScope().isThisExpr() || fieldAccess.getScope().toString().contains(".this")) {
                        thisAssignedNames.add(fieldAccess.getNameAsString());
                    }
                }
                if (assign.getOperator() == AssignExpr.Operator.ASSIGN
                        && assign.getTarget().isNameExpr()
                        && assign.getValue().isNameExpr()) {
                    String left = assign.getTarget().asNameExpr().getNameAsString();
                    String right = assign.getValue().asNameExpr().getNameAsString();
                    if (left.equals(right)) {
                        int assignLine = assign.getRange().map(r -> r.begin.line).orElse(line);
                        selfAssignments.add(new SelfAssignment(left, assignLine));
                    }
                }
            });
            body.findAll(VariableDeclarator.class).forEach(variable -> variable.getInitializer().ifPresent(initializer -> {
                if (initializer.isNameExpr() && variable.getNameAsString().equals(initializer.asNameExpr().getNameAsString())) {
                    int assignLine = variable.getRange().map(r -> r.begin.line).orElse(line);
                    selfAssignments.add(new SelfAssignment(variable.getNameAsString(), assignLine));
                }
            }));

            Map<String, Integer> callCounts = new HashMap<>();
            List<CallSiteFacts> unqualifiedCalls = new ArrayList<>();
            Set<ScopedCallFacts> scopedCalls = new LinkedHashSet<>();
            List<CallSiteFacts> expressionStatementCalls = new ArrayList<>();
            List<InvocationFacts> invocations = new ArrayList<>();
            List<ReflectionLookup> reflectionLookups = new ArrayList<>();
            Set<NameUse> nameUses = new LinkedHashSet<>();
            Set<FieldAccessUse> thisFieldAccesses = new LinkedHashSet<>();
            Set<String> qualifiedFieldAccessNames = new LinkedHashSet<>();
            Set<QualifiedFieldRef> qualifiedFieldRefs = new LinkedHashSet<>();
            body.findAll(MethodCallExpr.class).forEach(call -> invocations.add(new InvocationFacts(
                    call.getNameAsString(),
                    call.getArguments().stream().map(Expression::toString).map(DeclarationRiskAnalyzer::normalize).toList(),
                    call.getRange().map(r -> r.begin.line).orElse(line),
                    call.getScope().isPresent())));
            body.findAll(MethodCallExpr.class).stream()
                    .filter(call -> call.findAll(MethodCallExpr.class).size() == 1)
                    .forEach(call -> {
                        String normalizedCall = normalize(call.toString());
                        callCounts.merge(normalizedCall, 1, Integer::sum);

                        if (call.getScope().isEmpty()) {
                            int callLine = call.getRange().map(r -> r.begin.line).orElse(line);
                            CallSiteFacts facts = new CallSiteFacts(call.getNameAsString(), callLine);
                            unqualifiedCalls.add(facts);
                            if (call.getParentNode().filter(ExpressionStmt.class::isInstance).isPresent()) {
                                expressionStatementCalls.add(facts);
                            }
                        } else if (call.getScope().get().isNameExpr()) {
                            int callLine = call.getRange().map(r -> r.begin.line).orElse(line);
                            scopedCalls.add(new ScopedCallFacts(
                                    call.getScope().get().asNameExpr().getNameAsString(),
                                    call.getNameAsString(),
                                    call.getArguments().size(),
                                    callLine));
                        }

                        if ((call.getNameAsString().equals("getMethod") || call.getNameAsString().equals("getDeclaredMethod"))
                                && !call.getArguments().isEmpty()
                                && call.getArgument(0).isStringLiteralExpr()) {
                            int callLine = call.getRange().map(r -> r.begin.line).orElse(line);
                            String methodNameLiteral = call.getArgument(0).asStringLiteralExpr().getValue();
                            reflectionLookups.add(new ReflectionLookup(methodNameLiteral, callLine));
                        }
                    });
            body.findAll(NameExpr.class).forEach(nameExpr -> {
                int useLine = nameExpr.getRange().map(r -> r.begin.line).orElse(line);
                nameUses.add(new NameUse(nameExpr.getNameAsString(), useLine));
            });
            body.findAll(com.github.javaparser.ast.expr.FieldAccessExpr.class).forEach(access -> {
                qualifiedFieldAccessNames.add(access.getNameAsString());
                qualifiedFieldRefs.add(new QualifiedFieldRef(access.getScope().toString(), access.getNameAsString(),
                        access.getRange().map(r -> r.begin.line).orElse(line)));
                if (access.getScope().isThisExpr() || access.getScope().toString().contains(".this")) {
                    int accessLine = access.getRange().map(r -> r.begin.line).orElse(line);
                    thisFieldAccesses.add(new FieldAccessUse(access.getNameAsString(), accessLine));
                }
            });

            Set<String> localNames = new LinkedHashSet<>();
            Map<String, String> variableTypes = new LinkedHashMap<>();
            for (Parameter parameter : callable.getParameters()) {
                localNames.add(parameter.getNameAsString());
                variableTypes.put(parameter.getNameAsString(), normalize(parameter.getType().asString()));
            }
            body.findAll(VariableDeclarator.class).forEach(variable -> {
                localNames.add(variable.getNameAsString());
                variableTypes.put(variable.getNameAsString(), normalize(variable.getType().asString()));
            });
            Set<String> inScopeNames = new LinkedHashSet<>(localNames);
            inScopeNames.add(methodName);

            boolean returnsVoid = callable instanceof MethodDeclaration methodDeclaration
                    ? methodDeclaration.getType().isVoidType()
                    : false;
            String returnType = callable instanceof MethodDeclaration methodDeclaration
                    ? normalize(methodDeclaration.getType().asString())
                    : "";
            Set<String> parameterNames = new LinkedHashSet<>();
            for (Parameter parameter : callable.getParameters()) {
                parameterNames.add(parameter.getNameAsString());
            }
            String returnExpression = callable instanceof MethodDeclaration methodDeclaration
                    && body.getStatements().size() == 1
                    && body.getStatement(0).isReturnStmt()
                    && body.getStatement(0).asReturnStmt().getExpression().isPresent()
                    ? normalize(body.getStatement(0).asReturnStmt().getExpression().get().toString())
                    : null;
            boolean mutatedParameters = body.findAll(AssignExpr.class).stream()
                    .filter(assign -> assign.getTarget().isNameExpr())
                    .map(assign -> assign.getTarget().asNameExpr().getNameAsString())
                    .anyMatch(parameterNames::contains)
                    || body.findAll(com.github.javaparser.ast.expr.UnaryExpr.class).stream()
                    .map(unary -> unary.getExpression())
                    .filter(Expression::isNameExpr)
                    .map(expr -> expr.asNameExpr().getNameAsString())
                    .anyMatch(parameterNames::contains);
            boolean overrideAnnotation = callable.getAnnotations().stream()
                    .anyMatch(annotation -> annotation.getNameAsString().equals("Override"));
            boolean staticModifier = callable.getModifiers().stream()
                    .anyMatch(modifier -> modifier.getKeyword() == Modifier.Keyword.STATIC);
            boolean publicOrProtected = callable.getModifiers().stream()
                    .map(Modifier::getKeyword)
                    .anyMatch(keyword -> keyword == Modifier.Keyword.PUBLIC || keyword == Modifier.Keyword.PROTECTED);
            String bodyText = body.toString();
            boolean usesThisOrSuper = Pattern.compile("\\b(this|super)\\b").matcher(bodyText).find();

            return new MethodFacts(
                    file.absolutePath().toString(),
                    file.relativePath(),
                    container,
                    signature,
                    methodName,
                    arity,
                    returnType,
                    line,
                    id,
                    normalize(body.toString()),
                    selfAssignments,
                    callCounts,
                    List.copyOf(invocations),
                    localNames,
                    variableTypes,
                    inScopeNames,
                    nameUses,
                    new LinkedHashSet<>(unqualifiedCalls),
                    scopedCalls,
                    new LinkedHashSet<>(expressionStatementCalls),
                    thisFieldAccesses,
                    qualifiedFieldAccessNames,
                    qualifiedFieldRefs,
                    List.copyOf(reflectionLookups),
                    parameterNames,
                    returnExpression,
                    returnsVoid,
                    mutatedParameters,
                    overrideAnnotation,
                    staticModifier,
                    publicOrProtected,
                    usesThisOrSuper,
                    assignedNames,
                    thisAssignedNames);
        }
    }

    private sealed interface MemberFacts permits FieldFacts, MethodFacts {
        String file();
        String container();
        String fingerprint();
        String displaySignature();
        int line();
    }

    private record FieldFacts(
            String file,
            String relativePath,
            String container,
            String name,
            String type,
            boolean staticModifier,
            String initializer,
            int line) implements MemberFacts {
        private String id() {
            return relativePath + "::" + container + "::" + name;
        }

        @Override
        public String fingerprint() {
            return "FIELD|" + name + "|" + initializer;
        }

        @Override
        public String displaySignature() {
            return name;
        }
    }

    private record MethodFacts(
            String file,
            String relativePath,
            String container,
            String signature,
            String methodName,
            int arity,
            String returnType,
            int line,
            String id,
            String bodyFingerprint,
            List<SelfAssignment> selfAssignments,
            Map<String, Integer> callCounts,
            List<InvocationFacts> invocations,
            Set<String> localNames,
            Map<String, String> variableTypes,
            Set<String> inScopeNames,
            Set<NameUse> nameUses,
            Set<CallSiteFacts> unqualifiedCalls,
            Set<ScopedCallFacts> scopedCalls,
            Set<CallSiteFacts> expressionStatementCalls,
            Set<FieldAccessUse> thisFieldAccesses,
            Set<String> qualifiedFieldAccessNames,
            Set<QualifiedFieldRef> qualifiedFieldRefs,
            List<ReflectionLookup> reflectionLookups,
            Set<String> parameterNames,
            String returnExpression,
            boolean returnsVoid,
            boolean mutatedParameters,
            boolean overrideAnnotation,
            boolean staticModifier,
            boolean publicOrProtected,
            boolean usesThisOrSuper,
            Set<String> assignedNames,
            Set<String> thisAssignedNames) implements MemberFacts {
        private Set<String> calledNames() {
            Set<String> names = new LinkedHashSet<>();
            for (CallSiteFacts callSite : unqualifiedCalls) {
                names.add(callSite.name());
            }
            return names;
        }

        private Set<String> usedNames() {
            Set<String> names = new LinkedHashSet<>();
            for (NameUse nameUse : nameUses) {
                names.add(nameUse.name());
            }
            return names;
        }

        private boolean usesFieldOnlyQualified(String fieldName) {
            return qualifiedFieldAccessNames.contains(fieldName) && !usedNames().contains(fieldName);
        }

        @Override
        public String fingerprint() {
            return "METHOD|" + signature + "|" + bodyFingerprint;
        }

        @Override
        public String displaySignature() {
            return signature;
        }
    }

    private record SelfAssignment(String name, int line) {
    }

    private record CallSiteFacts(String name, int line) {
    }

    private record ReflectionLookup(String methodName, int line) {
    }

    private record NameUse(String name, int line) {
    }

    private record ScopedCallFacts(String scopeName, String methodName, int arity, int line) {
    }

    private record FieldAccessUse(String name, int line) {
    }

    private record QualifiedFieldRef(String scopeName, String fieldName, int line) {
    }

    private record InvocationFacts(String callName, List<String> argTexts, int line, boolean scoped) {
    }

    private static Optional<CompilationUnit> parseCompilationUnit(String content) {
        Optional<CompilationUnit> direct = parse(content);
        if (direct.isPresent()) {
            return direct;
        }

        String withoutPackages = content.replaceAll("(?m)^\\s*package\\s+[A-Za-z0-9_$.]+\\s*;\\s*$", "");
        Optional<CompilationUnit> noPackage = parse(withoutPackages);
        if (noPackage.isPresent()) {
            return noPackage;
        }

        String wrapped = "class __RefactorCheckWrapper {\n"
                + withoutPackages.replaceAll("(?m)^\\s*import\\s+[A-Za-z0-9_$.]+\\s*;\\s*$", "")
                + "\n}";
        return parse(wrapped);
    }


    private static Optional<CompilationUnit> parse(String content) {
        try {
            return Optional.of(StaticJavaParser.parse(content));
        } catch (ParseProblemException ex) {
            return Optional.empty();
        }
    }

    private static boolean looksLikeRecordPromotionSnippet(String beforeContent, String afterContent) {
        String beforeNormalized = normalize(beforeContent);
        String afterNormalized = normalize(afterContent);
        return beforeNormalized.contains("class")
                && afterNormalized.contains("record")
                && !afterNormalized.contains("publicclass")
                && !afterNormalized.contains("publicinterface");
    }

    private static String resolveContainer(Node node, CompilationUnit cu) {
        String packageName = cu.getPackageDeclaration().map(pd -> pd.getNameAsString()).orElse("");
        List<String> owners = new ArrayList<>();
        Node current = node;
        while (current != null) {
            if (current instanceof TypeDeclaration<?> typeDeclaration) {
                owners.add(typeDeclaration.getNameAsString());
            }
            current = current.getParentNode().orElse(null);
        }

        String ownerPart;
        if (owners.isEmpty()) {
            ownerPart = "<anonymous>";
        } else {
            StringBuilder builder = new StringBuilder();
            for (int i = owners.size() - 1; i >= 0; i--) {
                if (builder.length() > 0) {
                    builder.append('.');
                }
                builder.append(owners.get(i));
            }
            ownerPart = builder.toString();
        }

        if (packageName.isBlank()) {
            return ownerPart;
        }
        return packageName + "." + ownerPart;
    }

    private static String signature(CallableDeclaration<?> callable) {
        List<String> params = callable.getParameters().stream()
                .map(Parameter::getType)
                .map(type -> normalize(type.asString()))
                .toList();
        return callable.getNameAsString() + "(" + String.join(",", params) + ")";
    }

    private static String normalize(String text) {
        return text.replaceAll("\\s+", " ").trim().toLowerCase(Locale.ROOT);
    }

    private static String compact(String text) {
        return normalize(text).replace(" ", "");
    }

    private static String simpleName(String container) {
        int idx = container.lastIndexOf('.');
        return idx >= 0 ? container.substring(idx + 1) : container;
    }

    private static Map<String, Integer> publicClassCounts(String content, Pattern publicClassPattern) {
        Map<String, Integer> counts = new LinkedHashMap<>();
        Matcher matcher = publicClassPattern.matcher(content);
        while (matcher.find()) {
            counts.merge(matcher.group(1), 1, Integer::sum);
        }
        return counts;
    }

    private static Set<String> sourceFieldNames(String content) {
        Set<String> fields = new LinkedHashSet<>();
        Pattern field = Pattern.compile("(?m)^\\s*(?:private|protected|public)\\s+([^;=\\n()]*?)\\s+([A-Za-z_$][\\w$]*)\\s*(?:=|;)");
        Matcher matcher = field.matcher(content);
        while (matcher.find()) {
            fields.add(matcher.group(2));
        }
        return fields;
    }

    private static Map<String, Integer> sourceNonStaticFieldLines(String content) {
        Map<String, Integer> fields = new LinkedHashMap<>();
        Pattern field = Pattern.compile("(?m)^\\s*(?:private|protected|public)\\s+([^;=\\n()]*?)\\s+([A-Za-z_$][\\w$]*)\\s*(?:=|;)");
        Matcher matcher = field.matcher(content);
        while (matcher.find()) {
            String prefix = normalize(matcher.group(1));
            if (prefix.contains(" static ") || prefix.startsWith("static ")) {
                continue;
            }
            String name = matcher.group(2);
            fields.putIfAbsent(name, lineOf(content, matcher.start()));
        }
        return fields;
    }

    private static boolean hasSourceLocalDeclaration(String content, String name) {
        Pattern declaration = Pattern.compile("\\b(?:final\\s+)?[A-Za-z_$][\\w$<>\\[\\].?,]*\\s+"
                + Pattern.quote(name) + "\\s*=");
        return declaration.matcher(content).find();
    }

    private static boolean hasSourceAssignment(String content, String name) {
        Pattern assignment = Pattern.compile("(?:\\bthis\\s*\\.\\s*" + Pattern.quote(name)
                + "|(?<![\\w$.])" + Pattern.quote(name) + ")\\s*=(?!=)");
        return assignment.matcher(content).find();
    }

    private static int countMatches(Pattern pattern, String content) {
        int count = 0;
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    private static boolean containsScopedCall(String content, String methodName) {
        Pattern scopedCall = Pattern.compile("\\.\\s*" + Pattern.quote(methodName) + "\\s*\\(");
        return scopedCall.matcher(content).find();
    }

    private static String stripRefactoringSuffix(String name) {
        for (String suffix : List.of("Refactored", "Renamed", "Updated", "Shifted", "Adjusted")) {
            int index = name.indexOf(suffix);
            if (index > 0) {
                return name.substring(0, index);
            }
        }
        return name;
    }

    private static String previousMethodHeader(String content, int offset) {
        int cursor = Math.min(offset, content.length());
        while (cursor > 0) {
            int lineStart = content.lastIndexOf('\n', cursor - 1);
            int nextLineStart = lineStart < 0 ? 0 : lineStart + 1;
            String line = content.substring(nextLineStart, cursor).trim();
            if (line.contains("(") && line.contains(")") && line.endsWith("{")) {
                return line;
            }
            cursor = lineStart < 0 ? 0 : lineStart;
        }
        return "";
    }

    private static boolean isConstructorLike(MethodFacts method) {
        return method.methodName().equals(simpleName(method.container()));
    }

    private static boolean sameOrNestedContainer(String methodContainer, String fieldContainer) {
        return methodContainer.equals(fieldContainer) || methodContainer.startsWith(fieldContainer + ".");
    }

    private static boolean isExplicitRefactoredApiName(String beforeName, String afterName) {
        if (beforeName.equals(afterName)) {
            return false;
        }
        return afterName.startsWith(beforeName + "Refactored")
                || afterName.startsWith(beforeName + "Extracted")
                || afterName.startsWith(beforeName + "Renamed")
                || afterName.startsWith(beforeName + "Helper")
                || afterName.startsWith(beforeName + "Updated")
                || afterName.startsWith(beforeName + "Shifted")
                || afterName.startsWith(beforeName + "Adjusted")
                || afterName.startsWith(beforeName + "_")
                || afterName.matches(Pattern.quote(beforeName) + ".*(?:Refactored|Extracted|Renamed|Updated|Shifted|Adjusted|_mini_|_m3).*");
    }

    private static boolean containsPrecedenceSensitiveOperator(String text) {
        return text.contains("+") || text.contains("-") || text.contains("*") || text.contains("/") || text.contains("%");
    }

    private static boolean isLiteralExpression(String expression) {
        return expression.startsWith("\"")
                || expression.startsWith("'")
                || expression.matches("-?\\d+[a-z]?")
                || expression.equals("true")
                || expression.equals("false")
                || expression.equals("null");
    }

    private static String fieldInitializer(String content, String fieldName) {
        Matcher matcher = Pattern.compile("\\b" + Pattern.quote(fieldName) + "\\s*=\\s*([^;]+);").matcher(content);
        return matcher.find() ? matcher.group(1).trim() : null;
    }

    private static int indexOfDereference(String content, String receiver, String method) {
        Matcher matcher = Pattern.compile("\\b" + Pattern.quote(receiver)
                + "\\s*\\.\\s*" + Pattern.quote(method) + "\\s*\\(").matcher(content);
        return matcher.find() ? matcher.start() : -1;
    }

    private static int overloadCount(String content, String methodName) {
        Matcher matcher = Pattern.compile("\\b(?:public|protected|private|static|final|abstract|synchronized|native|strictfp|\\s)+[A-Za-z_$][\\w$<>\\[\\]]*\\s+"
                + Pattern.quote(methodName) + "\\s*\\(").matcher(content);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    private static int lineOf(String content, int offset) {
        if (offset < 0) {
            return 1;
        }
        int line = 1;
        for (int i = 0; i < offset && i < content.length(); i++) {
            if (content.charAt(i) == '\n') {
                line++;
            }
        }
        return line;
    }

    private static boolean argumentsCanBindToSignature(List<String> arguments, String signature) {
        List<String> parameterTypes = signatureParameterTypes(signature);
        if (arguments.size() != parameterTypes.size()) {
            return false;
        }
        for (int i = 0; i < arguments.size(); i++) {
            if (!argumentCanBindToType(arguments.get(i), parameterTypes.get(i))) {
                return false;
            }
        }
        return true;
    }

    private static List<String> signatureParameterTypes(String signature) {
        int open = signature.indexOf('(');
        int close = signature.lastIndexOf(')');
        if (open < 0 || close <= open) {
            return List.of();
        }
        String params = signature.substring(open + 1, close).trim();
        if (params.isBlank()) {
            return List.of();
        }
        List<String> types = new ArrayList<>();
        for (String param : params.split(",")) {
            types.add(normalize(param));
        }
        return types;
    }

    private static boolean argumentCanBindToType(String argument, String type) {
        String arg = normalize(argument);
        String normalizedType = normalize(type);
        if (arg.contains("new object")) {
            return normalizedType.equals("object") || normalizedType.equals("java.lang.object");
        }
        if (arg.contains("new integer")) {
            return normalizedType.equals("integer")
                    || normalizedType.equals("java.lang.integer")
                    || normalizedType.equals("int")
                    || normalizedType.equals("number")
                    || normalizedType.equals("object")
                    || normalizedType.equals("java.lang.object");
        }
        if (arg.startsWith("\"")) {
            return normalizedType.equals("string")
                    || normalizedType.equals("java.lang.string")
                    || normalizedType.equals("object")
                    || normalizedType.equals("java.lang.object");
        }
        if (arg.startsWith("'")) {
            return normalizedType.equals("char")
                    || normalizedType.equals("character")
                    || normalizedType.equals("int")
                    || normalizedType.equals("integer")
                    || normalizedType.equals("object")
                    || normalizedType.equals("java.lang.object");
        }
        if (arg.matches("-?\\d+[l]?")) {
            return normalizedType.equals("byte")
                    || normalizedType.equals("short")
                    || normalizedType.equals("int")
                    || normalizedType.equals("integer")
                    || normalizedType.equals("long")
                    || normalizedType.equals("number")
                    || normalizedType.equals("object")
                    || normalizedType.equals("java.lang.object");
        }
        return true;
    }

    private static boolean sameTypeCategory(String declaredType, String expression) {
        String normalizedType = normalize(declaredType);
        if (expression.startsWith("\"")) {
            return normalizedType.equals("string");
        }
        if (expression.startsWith("'")) {
            return normalizedType.equals("char");
        }
        if (expression.matches("-?\\d+[l]")) {
            return normalizedType.equals("long");
        }
        if (expression.matches("-?\\d+")) {
            return normalizedType.equals("int")
                    || normalizedType.equals("integer")
                    || normalizedType.equals("short")
                    || normalizedType.equals("byte");
        }
        return true;
    }

    private static boolean looksLikeExplicitCast(String expression) {
        String trimmed = expression.stripLeading();
        return trimmed.startsWith("(")
                && trimmed.contains(")")
                && !trimmed.startsWith("((");
    }
}
