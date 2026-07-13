package com.refactorcheck.cli;

import com.refactorcheck.core.AnalysisRequest;
import com.refactorcheck.core.model.ImpactMode;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

final class CliArguments {
    private CliArguments() {
    }

    static AnalysisRequest parse(String[] args) {
        if (args == null || args.length == 0) {
            throw new IllegalArgumentException(usage());
        }

        Map<String, String> options = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            String current = args[i];
            if (!current.startsWith("--")) {
                throw new IllegalArgumentException("Invalid argument: " + current + "\n" + usage());
            }
            if (i + 1 >= args.length || args[i + 1].startsWith("--")) {
                throw new IllegalArgumentException("Missing value for argument: " + current + "\n" + usage());
            }
            options.put(current, args[++i]);
        }

        Path before = requiredPath(options, "--before");
        Path after = requiredPath(options, "--after");
        ImpactMode impactMode = ImpactMode.fromCli(options.get("--impact"));
        Path output = requiredPath(options, "--output");

        Path mapper = null;
        if (impactMode == ImpactMode.OFF) {
            mapper = requiredPath(options, "--mapper");
        }

        return new AnalysisRequest(before, after, impactMode, mapper, output);
    }

    private static Path requiredPath(Map<String, String> options, String key) {
        String value = options.get(key);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Missing required argument: " + key + "\n" + usage());
        }
        return Path.of(value);
    }

    static String usage() {
        return "Usage: java -jar refactorcheck.jar --before <file|dir> --after <file|dir> "
                + "--impact <on|off> [--mapper <mapper.json>] --output <report.json>";
    }
}
