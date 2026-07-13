package com.refactorcheck.core.input;

import com.refactorcheck.core.analysis.JavaModelBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class InputSnapshotLoader {
    private final JavaModelBuilder modelBuilder;

    public InputSnapshotLoader(JavaModelBuilder modelBuilder) {
        this.modelBuilder = modelBuilder;
    }

    public SourceSnapshot load(String label, Path inputPath) throws IOException {
        if (!Files.exists(inputPath)) {
            throw new IllegalArgumentException("Input path does not exist: " + inputPath);
        }

        Path normalized = inputPath.toAbsolutePath().normalize();
        List<SourceFileUnit> units = new ArrayList<>();
        Path root = Files.isDirectory(normalized) ? normalized : normalized.getParent();

        if (Files.isDirectory(normalized)) {
            try (var stream = Files.walk(normalized)) {
                stream.filter(path -> Files.isRegularFile(path) && path.toString().endsWith(".java"))
                        .sorted(Comparator.naturalOrder())
                        .forEach(path -> units.add(toUnit(root, path)));
            }
        } else {
            if (!normalized.toString().endsWith(".java")) {
                throw new IllegalArgumentException("Input file must be .java: " + normalized);
            }
            units.add(toUnit(root, normalized));
        }

        if (units.isEmpty()) {
            throw new IllegalArgumentException("No Java files found in " + normalized);
        }

        return modelBuilder.buildSnapshot(label, root, units);
    }

    private SourceFileUnit toUnit(Path root, Path filePath) {
        try {
            String content = Files.readString(filePath, StandardCharsets.UTF_8);
            String relative = root.relativize(filePath).toString().replace('\\', '/');
            return new SourceFileUnit(filePath.toAbsolutePath().normalize(), relative, content);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read source file: " + filePath, e);
        }
    }
}
