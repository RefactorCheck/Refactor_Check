package com.refactorcheck.cli;

import com.google.gson.Gson;
import com.refactorcheck.core.AnalysisRequest;
import com.refactorcheck.core.RefactorCheckEngine;
import com.refactorcheck.core.model.ImpactMode;
import com.refactorcheck.core.report.RefactorCheckReport;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public final class BatchRefactorCheckCli {
    private static final Gson GSON = new Gson();
    private static final String[] RESULT_FIELDS = {
            "pair_id",
            "sample_id",
            "project_id",
            "model_label",
            "status",
            "consistent",
            "answer",
            "impactProvider",
            "fallbackUsed",
            "reasonsCount",
            "warningsCount",
            "report_path",
            "error"
    };

    private BatchRefactorCheckCli() {
    }

    public static void main(String[] args) {
        try {
            Config config = Config.parse(args);
            List<PairRow> pairs = readPairs(config.pairs);
            Map<String, ResultRow> rowsByPair = new ConcurrentHashMap<>(readExistingOkRows(config.results));
            List<PairRow> todo = pairs.stream()
                    .filter(pair -> !rowsByPair.containsKey(pair.pairId))
                    .toList();

            System.out.println("batch pairs=" + pairs.size()
                    + " existing_ok=" + rowsByPair.size()
                    + " todo=" + todo.size()
                    + " workers=" + config.workers);

            ExecutorService executor = Executors.newFixedThreadPool(config.workers);
            ExecutorCompletionService<ResultRow> completion = new ExecutorCompletionService<>(executor);
            for (PairRow pair : todo) {
                completion.submit(new PairTask(config, pair));
            }

            long started = System.currentTimeMillis();
            for (int i = 1; i <= todo.size(); i++) {
                Future<ResultRow> future = completion.take();
                ResultRow row = future.get();
                rowsByPair.put(row.pairId, row);
                if (i % config.writeInterval == 0) {
                    writeResults(config.results, pairs, rowsByPair);
                    double elapsed = (System.currentTimeMillis() - started) / 1000.0;
                    System.out.printf("completed_new=%d/%d total_rows=%d elapsed=%.1fs%n",
                            i, todo.size(), rowsByPair.size(), elapsed);
                }
            }
            executor.shutdown();
            writeResults(config.results, pairs, rowsByPair);
            System.out.println("done results=" + config.results + " rows=" + rowsByPair.size());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }

    private static List<PairRow> readPairs(Path path) throws IOException {
        List<Map<String, String>> rows = readTsv(path);
        List<PairRow> pairs = new ArrayList<>();
        for (Map<String, String> row : rows) {
            pairs.add(new PairRow(
                    row.get("pair_id"),
                    row.get("sample_id"),
                    row.get("project_id"),
                    row.get("model_label"),
                    row.get("original_file"),
                    row.get("refactored_file"),
                    row.getOrDefault("status", "")));
        }
        return pairs;
    }

    private static Map<String, ResultRow> readExistingOkRows(Path path) throws IOException {
        Map<String, ResultRow> rows = new LinkedHashMap<>();
        if (!Files.exists(path)) {
            return rows;
        }
        for (Map<String, String> row : readTsv(path)) {
            if ("OK".equals(row.get("status"))) {
                ResultRow result = ResultRow.fromMap(row);
                rows.put(result.pairId, result);
            }
        }
        return rows;
    }

    private static List<Map<String, String>> readTsv(Path path) throws IOException {
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        List<Map<String, String>> rows = new ArrayList<>();
        if (lines.isEmpty()) {
            return rows;
        }
        String[] header = lines.get(0).split("\t", -1);
        for (int i = 1; i < lines.size(); i++) {
            if (lines.get(i).isEmpty()) {
                continue;
            }
            String[] values = lines.get(i).split("\t", -1);
            Map<String, String> row = new LinkedHashMap<>();
            for (int j = 0; j < header.length; j++) {
                row.put(header[j], j < values.length ? values[j] : "");
            }
            rows.add(row);
        }
        return rows;
    }

    private static void writeResults(Path path, List<PairRow> pairs, Map<String, ResultRow> rowsByPair) throws IOException {
        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }
        List<String> lines = new ArrayList<>();
        lines.add(String.join("\t", RESULT_FIELDS));
        for (PairRow pair : pairs) {
            ResultRow row = rowsByPair.get(pair.pairId);
            if (row != null) {
                lines.add(row.toTsv());
            }
        }
        Files.write(path, lines, StandardCharsets.UTF_8);
    }

    private static String clean(String value) {
        if (value == null) {
            return "";
        }
        return value.replace('\t', ' ').replace('\n', ' ').replace('\r', ' ');
    }

    private record Config(Path pairs, Path outputRoot, Path results, int workers, int writeInterval) {
        static Config parse(String[] args) {
            Map<String, String> values = new LinkedHashMap<>();
            for (int i = 0; i < args.length; i++) {
                if (!args[i].startsWith("--")) {
                    throw new IllegalArgumentException("Unexpected argument: " + args[i]);
                }
                if (i + 1 >= args.length) {
                    throw new IllegalArgumentException("Missing value for " + args[i]);
                }
                values.put(args[i].substring(2), args[++i]);
            }
            Path pairs = requiredPath(values, "pairs");
            Path outputRoot = requiredPath(values, "output-root");
            Path results = values.containsKey("results")
                    ? Path.of(values.get("results"))
                    : outputRoot.resolve("results.tsv");
            int workers = Math.max(1, Math.min(4, Integer.parseInt(values.getOrDefault("workers", "4"))));
            int writeInterval = Math.max(1, Integer.parseInt(values.getOrDefault("write-interval", "100")));
            return new Config(pairs, outputRoot, results, workers, writeInterval);
        }

        private static Path requiredPath(Map<String, String> values, String key) {
            String value = values.get(key);
            if (value == null || value.isBlank()) {
                throw new IllegalArgumentException("Missing --" + key);
            }
            return Path.of(value);
        }
    }

    private record PairRow(
            String pairId,
            String sampleId,
            String projectId,
            String modelLabel,
            String originalFile,
            String refactoredFile,
            String status) {
    }

    private record ResultRow(
            String pairId,
            String sampleId,
            String projectId,
            String modelLabel,
            String status,
            String consistent,
            String answer,
            String impactProvider,
            String fallbackUsed,
            String reasonsCount,
            String warningsCount,
            String reportPath,
            String error) {
        static ResultRow fromMap(Map<String, String> row) {
            return new ResultRow(
                    row.get("pair_id"),
                    row.get("sample_id"),
                    row.get("project_id"),
                    row.get("model_label"),
                    row.get("status"),
                    row.get("consistent"),
                    row.get("answer"),
                    row.get("impactProvider"),
                    row.get("fallbackUsed"),
                    row.get("reasonsCount"),
                    row.get("warningsCount"),
                    row.get("report_path"),
                    row.get("error"));
        }

        String toTsv() {
            return String.join("\t",
                    clean(pairId),
                    clean(sampleId),
                    clean(projectId),
                    clean(modelLabel),
                    clean(status),
                    clean(consistent),
                    clean(answer),
                    clean(impactProvider),
                    clean(fallbackUsed),
                    clean(reasonsCount),
                    clean(warningsCount),
                    clean(reportPath),
                    clean(error));
        }
    }

    private static final class PairTask implements Callable<ResultRow> {
        private final Config config;
        private final PairRow pair;

        private PairTask(Config config, PairRow pair) {
            this.config = config;
            this.pair = pair;
        }

        @Override
        public ResultRow call() {
            Path report = config.outputRoot.resolve("reports").resolve(pair.pairId + ".json");
            String reportPath = report.toString();
            if (!"ok".equals(pair.status)) {
                return errorRow("MISSING_INPUT", reportPath, "pair input status is not ok");
            }
            if (Files.exists(report)) {
                try {
                    return successRow(GSON.fromJson(Files.readString(report, StandardCharsets.UTF_8), RefactorCheckReport.class), reportPath);
                } catch (Exception ignored) {
                    
                }
            }
            try {
                RefactorCheckEngine engine = new RefactorCheckEngine();
                RefactorCheckReport generated = engine.run(new AnalysisRequest(
                        Path.of(pair.originalFile),
                        Path.of(pair.refactoredFile),
                        ImpactMode.ON,
                        null,
                        report));
                return successRow(generated, reportPath);
            } catch (Exception e) {
                return errorRow("RUN_ERROR", reportPath, e.getMessage());
            }
        }

        private ResultRow successRow(RefactorCheckReport report, String reportPath) {
            String consistent = Boolean.toString(report.consistent);
            String answer = report.consistent ? "Yes" : "No";
            int reasons = 0;
            if (report.methodFindings != null) {
                for (RefactorCheckReport.MethodFinding finding : report.methodFindings) {
                    if (finding.reasons != null) {
                        reasons += finding.reasons.size();
                    }
                }
            }
            int warnings = report.warnings == null ? 0 : report.warnings.size();
            String provider = report.tooling == null ? "NA" : report.tooling.impactProvider;
            String fallback = report.tooling == null ? "NA" : Boolean.toString(report.tooling.fallbackUsed);
            return new ResultRow(
                    pair.pairId,
                    pair.sampleId,
                    pair.projectId,
                    pair.modelLabel,
                    "OK",
                    consistent,
                    answer,
                    provider,
                    fallback,
                    Integer.toString(reasons),
                    Integer.toString(warnings),
                    reportPath,
                    "");
        }

        private ResultRow errorRow(String status, String reportPath, String error) {
            return new ResultRow(
                    pair.pairId,
                    pair.sampleId,
                    pair.projectId,
                    pair.modelLabel,
                    status,
                    "",
                    "",
                    "NA",
                    "NA",
                    "0",
                    "0",
                    reportPath,
                    error == null ? "" : error);
        }
    }
}
