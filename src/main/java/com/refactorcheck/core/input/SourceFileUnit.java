package com.refactorcheck.core.input;

import java.nio.file.Path;

public record SourceFileUnit(Path absolutePath, String relativePath, String content) {
}
