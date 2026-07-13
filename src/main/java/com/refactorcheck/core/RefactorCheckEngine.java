package com.refactorcheck.core;

import com.refactorcheck.core.analysis.CfgBuilder;
import com.refactorcheck.core.analysis.ExceptionAwareCfgBuilder;
import com.refactorcheck.core.analysis.JavaModelBuilder;
import com.refactorcheck.core.check.DeclarationRiskAnalyzer;
import com.refactorcheck.core.check.MethodCheckResult;
import com.refactorcheck.core.check.NodeMatcher;
import com.refactorcheck.core.check.NodeMatchingResult;
import com.refactorcheck.core.check.OrderSensitiveInconsistencyChecker;
import com.refactorcheck.core.impact.ImpactAnalysisResult;
import com.refactorcheck.core.impact.ImpactAnalyzer;
import com.refactorcheck.core.impact.InternalImpactAnalyzer;
import com.refactorcheck.core.impact.MapperImpactAnalyzer;
import com.refactorcheck.core.impact.ReExtractorImpactAnalyzer;
import com.refactorcheck.core.input.InputSnapshotLoader;
import com.refactorcheck.core.input.SourceSnapshot;
import com.refactorcheck.core.mapper.MapperParser;
import com.refactorcheck.core.mapper.MapperSpec;
import com.refactorcheck.core.model.MethodCfg;
import com.refactorcheck.core.model.MethodPair;
import com.refactorcheck.core.model.MethodRef;
import com.refactorcheck.core.report.RefactorCheckReport;
import com.refactorcheck.core.report.ReportWriter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class RefactorCheckEngine {
    private final InputSnapshotLoader snapshotLoader;
    private final MapperParser mapperParser;
    private final ImpactAnalyzer reExtractorImpactAnalyzer;
    private final ImpactAnalyzer internalImpactAnalyzer;
    private final CfgBuilder cfgBuilder;
    private final NodeMatcher nodeMatcher;
    private final OrderSensitiveInconsistencyChecker checker;
    private final DeclarationRiskAnalyzer declarationRiskAnalyzer;
    private final ReportWriter reportWriter;

    public RefactorCheckEngine() {
        this.snapshotLoader = new InputSnapshotLoader(new JavaModelBuilder());
        this.mapperParser = new MapperParser();
        this.reExtractorImpactAnalyzer = new ReExtractorImpactAnalyzer();
        this.internalImpactAnalyzer = new InternalImpactAnalyzer();
        this.cfgBuilder = new ExceptionAwareCfgBuilder();
        this.nodeMatcher = new NodeMatcher();
        this.checker = new OrderSensitiveInconsistencyChecker();
        this.declarationRiskAnalyzer = new DeclarationRiskAnalyzer();
        this.reportWriter = new ReportWriter();
    }

    public RefactorCheckReport run(AnalysisRequest request) {
        long start = System.currentTimeMillis();

        SourceSnapshot before = loadSnapshot("before", request.before());
        SourceSnapshot after = loadSnapshot("after", request.after());

        List<String> warnings = new ArrayList<>();
        ImpactAnalysisResult impactResult = resolveImpact(request, before, after, warnings);

        List<MethodPair> methodPairs = dedupePairs(impactResult.methodPairs());
        List<MethodCheckResult> results = new ArrayList<>();

        for (MethodPair pair : methodPairs) {
            MethodRef beforeRef = pair.before();
            MethodRef afterRef = pair.after();
            if (!before.methodsByKey().containsKey(beforeRef.methodKey())
                    || !after.methodsByKey().containsKey(afterRef.methodKey())) {
                warnings.add("Method pair skipped due to unresolved method key: " + beforeRef + " -> " + afterRef);
                continue;
            }

            MethodCfg beforeCfg = cfgBuilder.build(before, beforeRef);
            MethodCfg afterCfg = cfgBuilder.build(after, afterRef);
            NodeMatchingResult matching = nodeMatcher.match(beforeCfg, afterCfg);
            MethodCheckResult result = checker.check(pair, beforeCfg, afterCfg, matching);
            results.add(result);
        }

        RefactorCheckReport report = toReport(
                request,
                before,
                after,
                impactResult,
                results,
                warnings,
                System.currentTimeMillis() - start);
        applyDeclarationRiskFallback(report, before, after);
        reportWriter.write(request.output(), report);
        return report;
    }

    private SourceSnapshot loadSnapshot(String label, java.nio.file.Path path) {
        try {
            return snapshotLoader.load(label, path);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to load " + label + " snapshot from " + path + ": " + e.getMessage(), e);
        }
    }

    private ImpactAnalysisResult resolveImpact(
            AnalysisRequest request,
            SourceSnapshot before,
            SourceSnapshot after,
            List<String> warnings) {
        if (request.impactMode() == com.refactorcheck.core.model.ImpactMode.OFF) {
            MapperSpec mapperSpec = mapperParser.parse(request.mapper());
            ImpactAnalyzer mapperAnalyzer = new MapperImpactAnalyzer(mapperSpec);
            return mapperAnalyzer.analyze(before, after);
        }

        try {
            return reExtractorImpactAnalyzer.analyze(before, after);
        } catch (Exception reExtractorError) {
            warnings.add("ReExtractor failed. Fallback to internal analyzer: " + reExtractorError.getMessage());
            try {
                return internalImpactAnalyzer.analyze(before, after);
            } catch (Exception internalError) {
                warnings.add("Internal fallback analyzer failed: " + internalError.getMessage());
                return new ImpactAnalysisResult(List.of(), "NONE", true, List.of());
            }
        }
    }

    private List<MethodPair> dedupePairs(List<MethodPair> pairs) {
        Map<String, MethodPair> deduped = new LinkedHashMap<>();
        for (MethodPair pair : pairs) {
            String key = pair.before().methodKey() + "->" + pair.after().methodKey();
            deduped.putIfAbsent(key, pair);
        }
        return new ArrayList<>(deduped.values());
    }

    private RefactorCheckReport toReport(
            AnalysisRequest request,
            SourceSnapshot before,
            SourceSnapshot after,
            ImpactAnalysisResult impact,
            List<MethodCheckResult> results,
            List<String> warnings,
            long elapsedMillis) {
        RefactorCheckReport report = new RefactorCheckReport();
        report.warnings.addAll(warnings);
        report.warnings.addAll(impact.warnings());

        RefactorCheckReport.Summary summary = new RefactorCheckReport.Summary();
        summary.comparedMethodPairs = results.size();
        summary.inconsistentMethodPairs = (int) results.stream().filter(r -> !r.consistent()).count();
        summary.totalMatchedStatements = results.stream().mapToInt(MethodCheckResult::matchedNodeCount).sum();
        summary.elapsedMillis = elapsedMillis;

        RefactorCheckReport.Tooling tooling = new RefactorCheckReport.Tooling();
        tooling.impactMode = request.impactMode().name();
        tooling.impactProvider = impact.provider();
        tooling.fallbackUsed = impact.fallback();
        tooling.refactoringMinerStatus = "NOT_IMPLEMENTED";

        report.summary = summary;
        report.tooling = tooling;
        report.consistent = summary.inconsistentMethodPairs == 0;
        if (summary.comparedMethodPairs == 0) {
            report.consistent = true;
        }

        for (MethodCheckResult result : results) {
            RefactorCheckReport.MethodFinding finding = new RefactorCheckReport.MethodFinding();
            finding.before = toDescriptor(result.pair().before());
            finding.after = toDescriptor(result.pair().after());
            finding.consistent = result.consistent();
            finding.confidence = result.confidence();
            finding.beforeNodes = result.beforeNodeCount();
            finding.afterNodes = result.afterNodeCount();
            finding.matchedNodes = result.matchedNodeCount();
            finding.reasons.addAll(result.reasons());
            report.methodFindings.add(finding);
        }

        return report;
    }

    private void applyDeclarationRiskFallback(
            RefactorCheckReport report,
            SourceSnapshot before,
            SourceSnapshot after) {
        if (isSnapshotTextEquivalent(before, after)) {
            return;
        }

        List<DeclarationRiskAnalyzer.DeclarationRisk> risks = declarationRiskAnalyzer.analyze(before, after);
        if (risks.isEmpty()) {
            if (report.summary.comparedMethodPairs == 0) {
                report.consistent = true;
            }
            return;
        }

        for (DeclarationRiskAnalyzer.DeclarationRisk risk : risks) {
            RefactorCheckReport.MethodFinding finding = new RefactorCheckReport.MethodFinding();
            finding.before = syntheticDescriptor(risk.beforeFile(), risk.beforeContainer(), risk.signature(), risk.beforeLine());
            finding.after = syntheticDescriptor(risk.afterFile(), risk.afterContainer(), risk.signature(), risk.afterLine());
            finding.consistent = false;
            finding.confidence = 0.65;
            finding.beforeNodes = 0;
            finding.afterNodes = 0;
            finding.matchedNodes = 0;
            finding.reasons.add(risk.reason());
            report.methodFindings.add(finding);
            report.summary.comparedMethodPairs += 1;
            report.summary.inconsistentMethodPairs += 1;
        }

        report.consistent = false;
    }

    private boolean isSnapshotTextEquivalent(SourceSnapshot before, SourceSnapshot after) {
        if (before.files().size() != after.files().size()) {
            return false;
        }
        Map<String, String> beforeByPath = new LinkedHashMap<>();
        for (var file : before.files()) {
            beforeByPath.put(file.relativePath(), normalize(file.content()));
        }
        for (var file : after.files()) {
            String beforeContent = beforeByPath.get(file.relativePath());
            if (beforeContent == null) {
                return false;
            }
            if (!beforeContent.equals(normalize(file.content()))) {
                return false;
            }
        }
        return true;
    }

    private String normalize(String content) {
        return content.replaceAll("\\s+", " ").trim();
    }

    private RefactorCheckReport.MethodDescriptor toDescriptor(MethodRef ref) {
        RefactorCheckReport.MethodDescriptor descriptor = new RefactorCheckReport.MethodDescriptor();
        descriptor.file = ref.absolutePath();
        descriptor.container = ref.container();
        descriptor.signature = ref.signature();
        descriptor.startLine = ref.startLine();
        descriptor.endLine = ref.endLine();
        return descriptor;
    }

    private RefactorCheckReport.MethodDescriptor syntheticDescriptor(
            String file,
            String container,
            String signature,
            int line) {
        RefactorCheckReport.MethodDescriptor descriptor = new RefactorCheckReport.MethodDescriptor();
        descriptor.file = file;
        descriptor.container = container;
        descriptor.signature = signature;
        descriptor.startLine = line;
        descriptor.endLine = line;
        return descriptor;
    }
}
